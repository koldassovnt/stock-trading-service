package kz.nkoldassov.stocktrading.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import kz.nkoldassov.stocktrading.model.dto.UserPortfolioDto;
import kz.nkoldassov.stocktrading.service.UserStockService;
import org.apache.commons.lang3.StringUtils;

public class UserPortfolioController {

    private final UserStockService userStockService;

    public UserPortfolioController(UserStockService userStockService) {
        this.userStockService = userStockService;
    }

    public Handler loadUserPortfolio() {
        return ctx -> {
            Long userId = getUserIdLong(ctx);

            UserPortfolioDto portfolioDto = userStockService.loadPortfolio(userId);

            ctx.json(portfolioDto);
        };
    }

    private Long getUserIdLong(Context ctx) {

        String userIdStr = ctx.pathParam("userId");

        if (StringUtils.isBlank(userIdStr)) {
            throw new IllegalArgumentException("fGCmIVt4 :: userId from param is null");
        }

        return Long.parseLong(userIdStr);

    }

}