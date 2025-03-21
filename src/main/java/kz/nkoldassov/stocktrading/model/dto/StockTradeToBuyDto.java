package kz.nkoldassov.stocktrading.model.dto;

import java.math.BigDecimal;

public record StockTradeToBuyDto(Long userId,
                                 BigDecimal price,
                                 int quantity) {
}