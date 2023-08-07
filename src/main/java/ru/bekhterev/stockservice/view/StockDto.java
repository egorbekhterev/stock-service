package ru.bekhterev.stockservice.view;

import java.util.Date;

public record StockDto(String symbol, String exchange, String exchangeSuffix, String exchangeName,
                       String exchangeSegment, String exchangeSegmentName, String name, Date date, String type,
                       String iexId, String region, String currency, boolean isEnabled, String figi, String cik,
                       String lei) {
}
