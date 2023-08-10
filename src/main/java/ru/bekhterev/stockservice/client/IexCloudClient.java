package ru.bekhterev.stockservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.bekhterev.stockservice.exception.ServiceException;
import ru.bekhterev.stockservice.view.QuoteDto;
import ru.bekhterev.stockservice.view.StockDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IexCloudClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Value("${iexcloud.public.key}")
    private String iexCloudPublicKey;

    public List<StockDto> getSymbols() {
        Mono<List<StockDto>> monoList = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/REF_DATA")
                        .queryParam("token", iexCloudPublicKey)
                        .build())
                .retrieve()
                .bodyToFlux(StockDto.class)
                .collectList();
        return monoList.block();
    }

    public QuoteDto getQuote(String symbol) {
        String jsonArray = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/quote/%s", symbol))
                        .queryParam("token", iexCloudPublicKey)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS),
                        response -> Mono.error(new ServiceException("Too Many Requests", response.statusCode())))
                .bodyToMono(String.class)
                .retryWhen(Retry.indefinitely()
                        .filter(ServiceException.class::isInstance))
                .block();
        String jsonObject = jsonArray.substring(1, jsonArray.length() - 1);
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(jsonObject);
        } catch (JsonProcessingException e) {
            log.error("Error while processing mapping of quote: {}", e.getMessage());
        }
        return new QuoteDto(tree.get("symbol").asText(), tree.get("latestPrice").decimalValue(),
                tree.get("changePercent").decimalValue());
    }
}
