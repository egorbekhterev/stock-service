package ru.bekhterev.stockservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
        mapFromDto(stockList).forEach(stock -> {
            if (checkStockExist(stock)) {
                Stock foundStock = stockRepository.findStockBySymbol(stock.getSymbol()).get();
                foundStock.setName(stock.getName());
                foundStock.setEnabled(stock.isEnabled());
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
                        .withName(stockDto.name())
                        .withIsEnabled(stockDto.isEnabled())
                        .build())
                .toList();
    }

    private boolean checkStockExist(Stock stock) {
        return stockRepository.existsBySymbol(stock.getSymbol());
    }
}
