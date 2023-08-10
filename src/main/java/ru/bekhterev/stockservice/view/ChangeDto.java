package ru.bekhterev.stockservice.view;

import java.math.BigDecimal;

public record ChangeDto(String symbol, BigDecimal change) {
}
