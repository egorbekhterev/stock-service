package ru.bekhterev.stockservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String symbol;
    private String exchange;
    private String exchangeSuffix;
    private String exchangeName;
    private String exchangeSegment;
    private String exchangeSegmentName;
    @EqualsAndHashCode.Include
    private String name;
    private Date date;
    private String type;
    private String iexId;
    private String region;
    private String currency;
    @EqualsAndHashCode.Include
    private boolean isEnabled;
    private String figi;
    private String cik;
    private String lei;
}
