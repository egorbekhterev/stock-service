package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bekhterev.stockservice.client.IexCloudClient;
import ru.bekhterev.stockservice.view.QuoteDto;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class IexCloudService {

    private final IexCloudClient iexCloudClient;
    private final List<String> stockSymbols = new CopyOnWriteArrayList<>();
    @Value("${service.stock-quantity}")
    private Integer stockQuantity;

    public List<StockDto> getStockSymbols() {
        stockSymbols.clear();
        log.info("Stock symbols storage cleaned.");
        return iexCloudClient.getSymbols().stream()
                .filter(StockDto::isEnabled)
                .limit(stockQuantity)
                .map(stockDto -> {
                    stockSymbols.add(stockDto.symbol());
                    log.debug("StockDto saved in storage {}", stockDto);
                    return stockDto;
                })
                .toList();
    }

    //todo разобраться + проверить дебаг, что мы тут не блочимся
    public List<QuoteDto> getQuotes() {
        List<CompletableFuture<QuoteDto>> futures = stockSymbols.stream()
                .map(symbol -> CompletableFuture.supplyAsync(() -> {
                    QuoteDto quoteDto = iexCloudClient.getQuote(symbol);
                    log.debug("QuoteDto received {}", quoteDto);
                    return quoteDto;
                }))
                .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .join();
    }
}
