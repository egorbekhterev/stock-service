package ru.bekhterev.stockservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.bekhterev.stockservice.exception.ServiceException;
import ru.bekhterev.stockservice.view.QuoteDto;
import ru.bekhterev.stockservice.view.StockDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class IexCloudClient {

    private final WebClient webClient;

    @Value("${iexcloud.public.key}")
    private String iexCloudPublicKey;

    public Flux<StockDto> getSymbols() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/REF_DATA")
                        .queryParam("token", iexCloudPublicKey)
                        .build())
                .retrieve()
                .bodyToFlux(StockDto.class);
    }

    public Flux<QuoteDto> getQuote(String symbol) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/quote/%s", symbol))
                        .queryParam("token", iexCloudPublicKey)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS),
                        response -> Mono.error(new ServiceException("Too Many Requests", response.statusCode())))
                .bodyToFlux(QuoteDto.class)
                .retryWhen(Retry.indefinitely()
                        .filter(ServiceException.class::isInstance));
    }
}
