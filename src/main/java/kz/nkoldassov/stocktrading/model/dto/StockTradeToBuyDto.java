package kz.nkoldassov.stocktrading.model.dto;

import kz.nkoldassov.stocktrading.annotation.NotNull;
import kz.nkoldassov.stocktrading.annotation.PositivePrimitiveInt;

import java.math.BigDecimal;

public record StockTradeToBuyDto(@NotNull Long userId,
                                 @NotNull BigDecimal price,
                                 @PositivePrimitiveInt(includeZero = false) int quantity) {
}