package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bekhterev.stockservice.repository.QuoteRepository;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsDataService {

    private final QuoteRepository quoteRepository;

    public void getTop5ByLatestPrice() {
        CompletableFuture.runAsync(() -> quoteRepository.findTop5ByOrderByLatestPriceDescSymbolAsc()
                .subscribe(quote -> log.info("LATEST PRICE: " + quote.getSymbol() + " : " + quote.getLatestPrice())))
                .join();
    }

    public void getTop5ByChangePercent() {
        CompletableFuture.runAsync(() -> quoteRepository.findTop5ByOrderByChangePercentAsc()
                .subscribe(quote -> log.info("CHANGE PERCENT: " + quote.getSymbol() + " : " + quote.getChangePercent())))
                .join();
    }
}
