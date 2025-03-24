package kz.nkoldassov.stocktrading.model.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockBuyTradeQueue(Long id,
                                 Long userId,
                                 BigDecimal price,
                                 Long stockId,
                                 Integer occupiedId,
                                 LocalDateTime occupiedAt,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}