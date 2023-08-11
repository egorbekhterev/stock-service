package ru.bekhterev.stockservice.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDto map(Stock stock);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    Stock map(StockDto stockDto);

    List<Stock> map(List<StockDto> stockDtos);
}
