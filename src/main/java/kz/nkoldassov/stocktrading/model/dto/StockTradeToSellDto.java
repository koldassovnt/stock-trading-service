package kz.nkoldassov.stocktrading.model.dto;

import kz.nkoldassov.stocktrading.annotation.NotEmpty;
import kz.nkoldassov.stocktrading.annotation.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record StockTradeToSellDto(@NotNull Long userId,
                                  @NotEmpty List<Detail> stockTradeDetail) {

    public record Detail(@NotNull Long userStockId,
                         @NotNull BigDecimal price) {
    }

}