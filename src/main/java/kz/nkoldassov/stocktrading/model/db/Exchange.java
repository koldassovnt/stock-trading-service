package kz.nkoldassov.stocktrading.model.db;

import java.time.LocalDateTime;

public record Exchange(Long id,
                       String name,
                       String code,
                       String country,
                       String currency,
                       LocalDateTime createdAt) {
}