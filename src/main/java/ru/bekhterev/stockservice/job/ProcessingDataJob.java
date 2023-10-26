package ru.bekhterev.stockservice.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.bekhterev.stockservice.service.ProcessingDataService;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessingDataJob {

    private final ProcessingDataService iexCloudService;

    @Scheduled(fixedDelay = 3600 * 1000)
    public void onStartupProcessingStockDataJob() {
        CompletableFuture.supplyAsync(iexCloudService::saveStocks)
                .thenApply(Mono::subscribe)
                .thenAccept(disposable -> log.info("Processing stock data finished"))
                .join();
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void runProcessingQuoteDataJob() {
        CompletableFuture.supplyAsync(iexCloudService::saveQuotes)
                .thenApply(Mono::subscribe)
                .thenAccept(disposable -> log.info("Processing quote data finished"))
                .join();
    }
}
