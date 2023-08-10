package ru.bekhterev.stockservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayDeque;
import java.util.Deque;

@EnableAsync
@EnableScheduling
@Configuration
public class ApplicationConfiguration {

    @Bean
    public Deque<String> queue() {
        return new ArrayDeque<>();
    }
}
