package kz.nkoldassov.stocktrading.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record StockTradeToSellDto(Long userId,
                                  List<Detail> stockTradeDetail) {

    public record Detail(Long userStockId,
                         BigDecimal price) {
    }

}