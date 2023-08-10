package ru.bekhterev.stockservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.client.IexCloudClient;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private static final int BATCH_SIZE = 25;
//    private static final int TEMPORARY_MAX_SIZE = 200;

    private final IexCloudClient iexCloudService;
    private final QuoteService quoteService;
    private final StockService stockService;
    private final ChangeService changeService;
    private final Deque<String> queue;

    @PostConstruct
    public void saveStocksAsync() {
        stockService.saveStocks(iexCloudService.getSymbols());
        this.readJob();
    }

    @Async
    public CompletableFuture<Void> readJob() {
        return CompletableFuture.runAsync(() -> {
            stockService.getEnabledStocksSymbols().forEach(queue::addLast);
            List<QuoteDto> quotes = new ArrayList<>();
            while (!queue.isEmpty()) {
                QuoteDto quoteDto = iexCloudService.getQuote(queue.removeFirst());
                quotes.add(quoteDto);
                if (quotes.size() == BATCH_SIZE) {
                    quoteService.saveQuotes(quotes);
                    quotes.clear();
                }
            }
        }).thenComposeAsync(result -> readJob());
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void scheduleTop5ByLatestPrice() {
        log.info(quoteService.getTop5ByLatestPrice().toString());
        log.info(changeService.getTop5ByChange().toString());
    }
}
