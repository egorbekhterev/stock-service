package ru.bekhterev.stockservice.view;

import java.math.BigDecimal;

public record QuoteDto(String symbol, BigDecimal latestPrice, BigDecimal changePercent) {
}
