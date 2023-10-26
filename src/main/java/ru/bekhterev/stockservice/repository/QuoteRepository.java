package ru.bekhterev.stockservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.bekhterev.stockservice.entity.Quote;

import java.util.UUID;

@Repository
public interface QuoteRepository extends R2dbcRepository<Quote, UUID> {

    Flux<Quote> findTop5ByOrderByLatestPriceDescSymbolAsc();
    Flux<Quote> findTop5ByOrderByChangePercentAsc();
}
