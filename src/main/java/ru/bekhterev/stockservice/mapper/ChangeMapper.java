package ru.bekhterev.stockservice.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bekhterev.stockservice.entity.Change;
import ru.bekhterev.stockservice.view.ChangeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChangeMapper {

    ChangeDto map(Change change);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    Change map(ChangeDto changeDto);

    List<ChangeDto> map(List<Change> changes);
}
