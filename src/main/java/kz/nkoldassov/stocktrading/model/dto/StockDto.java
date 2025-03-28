package kz.nkoldassov.stocktrading.model.dto;

import java.math.BigDecimal;

public record StockDto(Long id,
                       String ticker,
                       String company,
                       BigDecimal currentPrice,
                       String currency,
                       Long marketCap,
                       Long volume,
                       String exchange) {
}