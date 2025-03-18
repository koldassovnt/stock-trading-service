package kz.nkoldassov.stocktrading.model.db;

import java.time.LocalDateTime;

public record Company(Long id,
                      String name,
                      Long industryId,
                      LocalDateTime createdAt) {
}