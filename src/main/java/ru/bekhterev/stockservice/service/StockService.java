package ru.bekhterev.stockservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.repository.StockRepository;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    public Stock getStockBySymbol(String symbol) {
        //todo
        return stockRepository.findStockBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException(
                "Symbol not found"
        ));
    }

    public List<String> getEnabledStocksSymbols() {
        return stockRepository.findAllByIsEnabled(true)
                .stream()
                .map(Stock::getSymbol)
                .toList();
    }

    public void saveStocks(List<StockDto> stockList) {
        mapFromDto(stockList).forEach(stock -> stockRepository.findStockBySymbol(stock.getSymbol())
                .ifPresentOrElse(stk -> {
                    stk.setName(stock.getName());
                    stk.setEnabled(stock.isEnabled());
                    stockRepository.save(stk);
                }, () -> stockRepository.save(stock)
        ));
        log.info("Stocks saved successfully");
    }

    private List<Stock> mapFromDto(List<StockDto> stockDtos) {
        return stockDtos.stream().map(stockDto -> Stock.builder()
                        .withSymbol(stockDto.symbol())
                        .withName(stockDto.name())
                        .withIsEnabled(stockDto.isEnabled())
                        .build())
                .toList();
    }
}
