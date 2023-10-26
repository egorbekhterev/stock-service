package ru.bekhterev.stockservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.view.QuoteDto;

@Mapper(componentModel = "spring")
public interface QuoteMapper {

    QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

    Quote map(QuoteDto quoteDto);
}
