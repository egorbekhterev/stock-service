package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.mapper.QuoteMapper;
import ru.bekhterev.stockservice.repository.QuoteRepository;
import ru.bekhterev.stockservice.view.ChangeDto;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private static final int LATEST_PRICE_SCALE = 4;

    private final QuoteRepository quoteRepository;
    private final ChangeService changeService;
    private final QuoteMapper quoteMapper;
    private final StockService stockService;

    public void saveQuotes(List<QuoteDto> dtoList) {
        List<Quote> quotes = getQuotesFromDto(dtoList);
        quotes.forEach(quote -> {
            Optional<Quote> optionalQuote = quoteRepository.findTopByStockOrderByRefreshTimeDesc(quote.getStock());
            optionalQuote.ifPresentOrElse(qt -> {
                //проверяем, изменилась ли latestPrice для котировки, чтобы определить, уместно ли добавлять
                // запись в таблицу quote для ведения журнала. Скейл нужен для соответствия numeric из БД.
                if (!qt.getLatestPrice().equals(quote.getLatestPrice().setScale(LATEST_PRICE_SCALE))) {
                    //логика создания дельты
                    Quote qte = quoteRepository.save(quote);
                    //считаем модуль изменения цены за акцию
                    BigDecimal change = qte.getLatestPrice().subtract(qt.getLatestPrice()).abs();
                    //сохраняем изменение цены, если дельта еще не записана в базу или перезаписываем дельту на самую
                    //актуальную, если такая дельта существует.
                    changeService.saveChange(new ChangeDto(qt.getStock().getSymbol(), change));
                }
            }, () -> quoteRepository.save(quote));
        });
    }

    public List<QuoteDto> getTop5ByLatestPrice() {
        List<Quote> quotes = quoteRepository.findTop5ByOrderByLatestPriceDescStockNameAsc();
        return quoteMapper.mapToDto(quotes);
    }

    private List<Quote> getQuotesFromDto(List<QuoteDto> quoteDtos) {
        List<Quote> quotes = quoteMapper.mapFromDto(quoteDtos);
        return quotes.stream()
                .map(quote -> {
                    String symbol = quote.getStock().getSymbol();
                    Stock stock = stockService.getStockBySymbol(symbol)
                            .orElse(quote.getStock());
                    return quote.toBuilder().stock(stock).build();
                })
                .toList();
    }
}
