package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Change;
import ru.bekhterev.stockservice.repository.ChangeRepository;
import ru.bekhterev.stockservice.view.ChangeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeService {

    private final ChangeRepository changeRepository;

    public void saveChange(ChangeDto changeDto) {
        changeRepository.findChangeBySymbol(changeDto.symbol())
                .ifPresentOrElse(change -> {
                    change.setChange(changeDto.change());
                    changeRepository.save(change);
                }, () -> changeRepository.save(mapFromDto(changeDto)));
    }

    public List<ChangeDto> getTop5ByChange() {
        return mapToDto(changeRepository.findTop5ByOrderByChangeDesc());
    }

    private List<ChangeDto> mapToDto(List<Change> changes) {
        return changes.stream().map(change ->
                new ChangeDto(change.getSymbol(), change.getChange()))
                .toList();
    }

    private Change mapFromDto(ChangeDto changeDto) {
        return Change.builder()
                .withSymbol(changeDto.symbol())
                .withChange(changeDto.change())
                .build();
    }
}
