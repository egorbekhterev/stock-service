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
@ToString
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String symbol;
    private String exchange;
    private String exchangeSuffix;
    private String exchangeName;
    private String exchangeSegment;
    private String exchangeSegmentName;
    private String name;
    private Date date;
    private String type;
    private String iexId;
    private String region;
    private String currency;
    private boolean isEnabled;
    private String figi;
    private String cik;
    private String lei;
}
