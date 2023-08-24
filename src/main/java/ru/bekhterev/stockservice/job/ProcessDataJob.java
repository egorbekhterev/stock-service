package ru.bekhterev.stockservice.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bekhterev.stockservice.service.IexCloudService;
import ru.bekhterev.stockservice.service.QuoteService;
import ru.bekhterev.stockservice.service.StockService;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDataJob {

    private final StockService stockService;
    private final QuoteService quoteService;
    private final IexCloudService iexCloudService;

    @Scheduled(fixedDelay = 3600 * 1000)
    public void onStartupProcessingStockDataJob() {
        CompletableFuture.supplyAsync(iexCloudService::getStockSymbols)
                .thenAccept(stockService::saveStocks)
                .join();
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void runProcessingQuoteDataJob() {
        CompletableFuture.supplyAsync(iexCloudService::getQuotes)
                .thenAccept(quoteService::saveQuotes)
                .join();
    }
}
