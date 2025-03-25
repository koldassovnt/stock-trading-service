package kz.nkoldassov.stocktrading.model.dto;

import kz.nkoldassov.stocktrading.annotation.NotNull;
import kz.nkoldassov.stocktrading.annotation.PositivePrimitiveInt;

import java.math.BigDecimal;

public record StockOrderToBuyDto(@NotNull Long userId,
                                 @NotNull BigDecimal price,
                                 @NotNull String ticker,
                                 @PositivePrimitiveInt(includeZero = false) int quantity) {
}