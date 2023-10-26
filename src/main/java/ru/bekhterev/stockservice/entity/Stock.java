package ru.bekhterev.stockservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private BigInteger id;

    @EqualsAndHashCode.Include
    private String symbol;

    private String name;

    private boolean isEnabled;
}
