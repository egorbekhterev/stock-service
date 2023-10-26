package ru.bekhterev.stockservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.bekhterev.stockservice.entity.Stock;

import java.math.BigInteger;

@Repository
public interface StockRepository extends R2dbcRepository<Stock, BigInteger> {
}
