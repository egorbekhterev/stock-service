package ru.bekhterev.stockservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.entity.Stock;
import ru.bekhterev.stockservice.repository.QuoteRepository;
import ru.bekhterev.stockservice.repository.StockRepository;
import ru.bekhterev.stockservice.view.QuoteDto;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final StockRepository stockRepository;

    private final QuoteRepository quoteRepository;

    private final WebClient webClient;

    @Value("${iexcloud.public.key}")
    private String iexCloudPublicKey;

    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    @Async
    public void readJob() {
        getEnabledStocksSymbols().forEach(queue::offer);
        while (!queue.isEmpty()) {
            String jsonArray = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(String.format("/data/core/quote/%s", queue.poll()))
                            .queryParam("token", iexCloudPublicKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            // todo хуета
            String jsonObject = jsonArray.substring(1, jsonArray.length() - 1);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode tree = objectMapper.readTree(jsonObject);
                QuoteDto quoteDto = new QuoteDto(tree.get("symbol").asText(), tree.get("latestPrice").decimalValue(),
                        tree.get("changePercent").decimalValue());
                Quote quote = mapFromDto(quoteDto);
                quoteRepository.saveAndFlush(quote);
            } catch (Exception e) {
                // todo обработать
                throw new RuntimeException(e);
            }
        }
    }

    private List<String> getEnabledStocksSymbols() {
        return stockRepository.findAllByIsEnabled(true)
                .stream()
                .map(Stock::getSymbol)
                .toList();
    }

    private Quote mapFromDto(QuoteDto quoteDto) {
        return Quote.builder()
                .withSymbol(quoteDto.symbol())
                .withLatestPrice(quoteDto.latestPrice())
                .withChangePercent(quoteDto.changePercent())
                .build();
    }

    @Async
    public void writeJob() {
        //todo ага
    }
}
