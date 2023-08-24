package ru.bekhterev.stockservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.entity.Change;
import ru.bekhterev.stockservice.mapper.ChangeMapper;
import ru.bekhterev.stockservice.repository.ChangeRepository;
import ru.bekhterev.stockservice.view.ChangeDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void getTop5ByChange() {
        CompletableFuture.runAsync(() -> {
            List<Change> changes = changeRepository.findTop5ByOrderByChangeDesc();
            log.info("Top 5 quotes by price change:");
            changes.forEach(change -> log.info(change.getSymbol() + " : " + change.getChange()));
        });
    }
}
