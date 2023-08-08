package ru.bekhterev.stockservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode
public class Quote {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String symbol;

    private BigDecimal latestPrice;

    private BigDecimal changePercent;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private OffsetDateTime refreshTime = OffsetDateTime.now();
}
