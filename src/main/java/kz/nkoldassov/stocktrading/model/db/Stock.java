package kz.nkoldassov.stocktrading.model.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Stock(Long id,
                    String ticker,
                    Long companyId,
                    BigDecimal currentPrice,
                    String currency,
                    Long marketCap,
                    Long volume,
                    Long exchangeId,
                    LocalDateTime createdAt,
                    LocalDateTime updateAt) {
}