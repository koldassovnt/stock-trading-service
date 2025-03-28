package kz.nkoldassov.stocktrading.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record UserPortfolioDto(Long userId,
                               List<Detail> details) {

    public record Detail(String ticker,
                         BigDecimal price) {
    }

    public static UserPortfolioDto of(Long userId, List<Detail> details) {
        return new UserPortfolioDto(userId, details);
    }

}