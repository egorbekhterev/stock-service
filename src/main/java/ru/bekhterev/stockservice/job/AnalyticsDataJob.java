package ru.bekhterev.stockservice.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bekhterev.stockservice.service.AnalyticsDataService;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AnalyticsDataJob {

    private final AnalyticsDataService analyticsDataService;

    @Scheduled(cron = "*/5 * * * * *")
    public void scheduleTop5ByLatestPrice() {
        CompletableFuture.runAsync(() -> {
                    analyticsDataService.getTop5ByLatestPrice();
                    analyticsDataService.getTop5ByChangePercent();
                })
                .join();
    }
}
