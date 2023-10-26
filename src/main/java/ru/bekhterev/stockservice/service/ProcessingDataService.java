package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bekhterev.stockservice.client.IexCloudClient;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.mapper.QuoteMapper;
import ru.bekhterev.stockservice.mapper.StockMapper;
import ru.bekhterev.stockservice.repository.QuoteRepository;
import ru.bekhterev.stockservice.repository.StockRepository;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessingDataService {

    private final List<String> symbols = new CopyOnWriteArrayList<>();
    @Value("${service.stock-quantity}")
    private Integer stockQuantity;

    private final IexCloudClient iexCloudClient;
    private final StockRepository stockRepository;
    private final QuoteRepository quoteRepository;

    public Mono<Void> saveStocks() {
        symbols.clear();
        log.debug("Symbols storage cleared.");
        return iexCloudClient.getSymbols()
                .onErrorContinue((error, object) -> log.error("{}", error.getMessage()))
                .filter(StockDto::isEnabled)
                .take(stockQuantity)
                .map(this::mapFromDto)
                .map(stockRepository::save)
                .map(Mono::subscribe)
                .then();
    }

    private Stock mapFromDto(StockDto stockDto) {
        Stock stock = StockMapper.INSTANCE.map(stockDto);
        String symbol = stock.getSymbol();
        symbols.add(symbol);
        log.debug("Symbol saved in storage {}", symbol);
        return stock;
    }

    public Mono<Void> saveQuotes() {
        return Flux.fromIterable(symbols)
                .flatMap(iexCloudClient::getQuote)
                .onErrorContinue((error, object) -> log.error("{}", error.getMessage()))
                .map(QuoteMapper.INSTANCE::map)
                .map(quoteRepository::save)
                .map(Mono::subscribe)
                .then();
    }
}
