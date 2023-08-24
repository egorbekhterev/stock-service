package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.mapper.StockMapper;
import ru.bekhterev.stockservice.repository.StockRepository;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public Optional<Stock> getStockBySymbol(String symbol) {
        return stockRepository.findStockBySymbol(symbol);
    }

    public void saveStocks(List<StockDto> stockList) {
        List<Stock> stocks = stockMapper.map(stockList);
        stocks.forEach(stock -> stockRepository.findStockBySymbol(stock.getSymbol())
                .ifPresentOrElse(stk -> {
                    stk.setName(stock.getName());
                    stk.setEnabled(stock.isEnabled());
                    stockRepository.save(stk);
                }, () -> stockRepository.save(stock)
        ));
        log.info("Stocks saved successfully");
    }
}
