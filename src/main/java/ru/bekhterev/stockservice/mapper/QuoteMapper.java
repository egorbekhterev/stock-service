package ru.bekhterev.stockservice.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = StockMapper.class)
public interface QuoteMapper {

    @Mapping(source = "stock.symbol", target = "symbol")
    QuoteDto map(Quote quote);

    @InheritInverseConfiguration
    Quote map(QuoteDto quoteDto);

    List<Quote> mapFromDto(List<QuoteDto> quoteDtos);
}
