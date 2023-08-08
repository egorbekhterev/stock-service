package ru.bekhterev.stockservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.repository.StockRepository;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final WebClient webClient;

    private final StockRepository stockRepository;

    private final JobService jobService;


    @Value("${iexcloud.public.key}")
    private String iexCloudPublicKey;

    @PostConstruct
    public void saveStocksAsync() {
        Mono<List<StockDto>> monoList = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ref-data/symbols")
                        .queryParam("token", iexCloudPublicKey)
                        .build())
                .retrieve()
                .bodyToFlux(StockDto.class)
                .collectList();
        saveStocks(Objects.requireNonNull(monoList.block()));
        runParallelJobs();
    }

    private void runParallelJobs() {
        jobService.readJob();
    }

    private void saveStocks(List<StockDto> stockList) {
        mapFromDto(stockList).forEach(stock -> {
            if (checkStockExist(stock)) {
                Stock foundStock = stockRepository.findStockBySymbol(stock.getSymbol()).get();
                foundStock.setExchange(stock.getExchange());
                foundStock.setExchangeSuffix(stock.getExchangeSuffix());
                foundStock.setExchangeName(stock.getExchangeName());
                foundStock.setExchangeSegment(stock.getExchangeSegment());
                foundStock.setExchangeSegmentName(stock.getExchangeSegment());
                foundStock.setName(stock.getName());
                foundStock.setType(stock.getType());
                foundStock.setIexId(stock.getIexId());
                foundStock.setRegion(stock.getRegion());
                foundStock.setCurrency(stock.getCurrency());
                foundStock.setEnabled(stock.isEnabled());
                foundStock.setFigi(stock.getFigi());
                foundStock.setCik(stock.getCik());
                foundStock.setLei(stock.getLei());
            } else {
                try {
                    stockRepository.save(stock);
                } catch (DataAccessException e) {
                    log.error("Exception while saving stocks: {}", e.getMessage());
                }
            }
        });
        log.info("Stocks saved successfully");
    }

    private List<Stock> mapFromDto(List<StockDto> stockDtos) {
        return stockDtos.stream().map(stockDto -> Stock.builder()
                .withSymbol(stockDto.symbol())
                .withExchange(stockDto.exchange())
                .withExchangeSuffix(stockDto.exchangeSuffix())
                .withExchangeName(stockDto.exchangeName())
                .withExchangeSegment(stockDto.exchangeSegment())
                .withExchangeSegmentName(stockDto.exchangeSegmentName())
                .withName(stockDto.name())
                .withType(stockDto.type())
                .withIexId(stockDto.iexId())
                .withRegion(stockDto.region())
                .withCurrency(stockDto.currency())
                .withIsEnabled(stockDto.isEnabled())
                .withFigi(stockDto.figi())
                .withCik(stockDto.cik())
                .withLei(stockDto.lei())
                .build())
                .toList();
    }

    private boolean checkStockExist(Stock stock) {
        return stockRepository.existsBySymbol(stock.getSymbol());
    }
}
