package ru.bekhterev.stockservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bekhterev.stockservice.client.IexCloudClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartService {

    private final StockService stockService;
    private final JobService jobService;
    private final IexCloudClient iexCloudService;

    @PostConstruct
    public void saveStocksAsync() {
        stockService.saveStocks(iexCloudService.getSymbols());
        jobService.readJob();
    }
}
