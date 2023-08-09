package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.repository.QuoteRepository;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final StockService stockService;

    public List<Quote> saveQuotes(List<QuoteDto> dtoList) {
        return quoteRepository.saveAll(mapFromDto(dtoList));
    }

    public List<QuoteDto> getTop5ByLatestPrice() {
        return mapToDto(quoteRepository.findTop5ByOrderByLatestPriceDescStockNameAsc());
    }

    private List<QuoteDto> mapToDto(List<Quote> quotes) {
        return quotes.stream().map(quote ->
                new QuoteDto(quote.getStock().getSymbol(), quote.getLatestPrice(), quote.getChangePercent()))
                .toList();
    }

    private List<Quote> mapFromDto(List<QuoteDto> dtoList) {
        return dtoList.stream().map(quoteDto -> Quote.builder()
                .withStock(stockService.getStockBySymbol(quoteDto.symbol()))
                .withLatestPrice(quoteDto.latestPrice())
                .withChangePercent(quoteDto.changePercent())
                .build()).toList();
    }
}
