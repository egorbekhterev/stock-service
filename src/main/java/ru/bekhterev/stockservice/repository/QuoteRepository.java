package ru.bekhterev.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bekhterev.stockservice.entity.Quote;
import ru.bekhterev.stockservice.entity.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {

    List<Quote> findTop5ByOrderByLatestPriceDescStockNameAsc();

    Optional<Quote> findTopByStockOrderByRefreshTimeDesc(Stock stock);
}
