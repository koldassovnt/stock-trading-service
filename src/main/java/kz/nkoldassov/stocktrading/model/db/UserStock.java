package kz.nkoldassov.stocktrading.model.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserStock(Long id,
                        Long userId,
                        Long stockId,
                        BigDecimal price,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
}