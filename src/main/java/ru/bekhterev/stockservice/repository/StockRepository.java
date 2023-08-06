package ru.bekhterev.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bekhterev.stockservice.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Long, Stock> {
}
