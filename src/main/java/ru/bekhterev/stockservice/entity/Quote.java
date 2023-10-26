package ru.bekhterev.stockservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private BigInteger id;

    private String symbol;

    private BigDecimal latestPrice;

    private BigDecimal changePercent;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime refreshTime = LocalDateTime.now();
}
