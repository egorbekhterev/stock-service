package ru.bekhterev.stockservice.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfiguration {

    private static final String BASE_URL = "https://api.iex.cloud/v1/data/CORE";
    private static final int TIMEOUT_INTERVAL = 5000;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_INTERVAL)
                .responseTimeout(Duration.ofMillis(TIMEOUT_INTERVAL))
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(TIMEOUT_INTERVAL, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_INTERVAL, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
