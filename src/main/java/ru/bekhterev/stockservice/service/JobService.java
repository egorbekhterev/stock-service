package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.client.IexCloudClient;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private static final int BATCH_SIZE = 100;

    private final IexCloudClient iexCloudService;
    private final QuoteService quoteService;
    private final StockService stockService;
    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    @Async
    public void readJob() {
        stockService.getEnabledStocksSymbols().forEach(queue::offer);
        List<QuoteDto> quotes = new ArrayList<>();
        while (!queue.isEmpty()) {
            QuoteDto quoteDto = iexCloudService.getQuote(queue.poll());
            quotes.add(quoteDto);
            if (quotes.size() == BATCH_SIZE) {
                quoteService.saveQuotes(quotes);
                quotes.clear();
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void writeJob() {
        log.info(quoteService.getTop5ByLatestPrice().toString());
    }
}
