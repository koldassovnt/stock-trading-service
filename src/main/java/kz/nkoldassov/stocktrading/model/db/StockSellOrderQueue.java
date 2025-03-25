package kz.nkoldassov.stocktrading.model.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockSellOrderQueue(Long id,
                                  Long userId,
                                  Long userStockId,
                                  BigDecimal price,
                                  Integer occupiedId,
                                  LocalDateTime occupiedAt,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}