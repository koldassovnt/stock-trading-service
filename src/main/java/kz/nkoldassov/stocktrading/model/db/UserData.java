package kz.nkoldassov.stocktrading.model.db;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserData(Long id,
                       String name,
                       String surname,
                       LocalDate birthDate,
                       LocalDateTime createdAt) {
}