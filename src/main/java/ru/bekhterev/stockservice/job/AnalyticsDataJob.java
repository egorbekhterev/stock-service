package ru.bekhterev.stockservice.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bekhterev.stockservice.service.ChangeService;
import ru.bekhterev.stockservice.service.QuoteService;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AnalyticsDataJob {

    private final QuoteService quoteService;
    private final ChangeService changeService;

    @Scheduled(cron = "*/5 * * * * *")
    public void scheduleTop5ByLatestPrice() {
        CompletableFuture.runAsync(() -> {
                    quoteService.getTop5ByLatestPrice();
                    changeService.getTop5ByChange();
                })
                .join();
    }
}
