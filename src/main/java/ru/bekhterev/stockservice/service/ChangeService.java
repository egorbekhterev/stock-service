package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.mapper.ChangeMapper;
import ru.bekhterev.stockservice.repository.ChangeRepository;
import ru.bekhterev.stockservice.view.ChangeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeService {

    private final ChangeRepository changeRepository;
    private final ChangeMapper changeMapper;

    public void saveChange(ChangeDto changeDto) {
        changeRepository.findChangeBySymbol(changeDto.symbol())
                .ifPresentOrElse(change -> {
                    change.setChange(changeDto.change());
                    changeRepository.save(change);
                }, () -> changeRepository.save(changeMapper.map(changeDto)));
    }

    public List<ChangeDto> getTop5ByChange() {
        return changeMapper.map(changeRepository.findTop5ByOrderByChangeDesc());
    }
}
