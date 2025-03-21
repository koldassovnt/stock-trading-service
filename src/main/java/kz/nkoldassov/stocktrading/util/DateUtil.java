package kz.nkoldassov.stocktrading.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class DateUtil {

    private DateUtil() {}

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

}