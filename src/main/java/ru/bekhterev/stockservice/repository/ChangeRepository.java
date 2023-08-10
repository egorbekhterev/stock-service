package ru.bekhterev.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bekhterev.stockservice.entity.Change;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeRepository extends JpaRepository<Change, BigInteger> {

    Optional<Change> findChangeBySymbol(String symbol);

    List<Change> findTop5ByOrderByChangeDesc();
}
