package kz.nkoldassov.stocktrading.model.db;

import java.time.LocalDateTime;

public record Industry(Long id,
                       String name,
                       LocalDateTime createdAt) {
}