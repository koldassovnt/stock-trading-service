package kz.nkoldassov.stocktrading.model.dto;

import java.math.BigDecimal;

public record StockTradeDto(Long userId,
                            String ticker,
                            BigDecimal price,
                            String currency) {
}