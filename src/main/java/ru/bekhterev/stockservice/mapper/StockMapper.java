package ru.bekhterev.stockservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.view.StockDto;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    @Mapping(target = "id", ignore = true)
    Stock map(StockDto stockDto);
}
