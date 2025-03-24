package kz.nkoldassov.stocktrading.controller;

import io.javalin.http.Handler;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToSellDto;
import kz.nkoldassov.stocktrading.service.StockTradeService;

public class StockTradeController {

    private final StockTradeService stockTradeService;

    public StockTradeController(StockTradeService stockTradeService) {
        this.stockTradeService = stockTradeService;
    }

    public Handler placeBuyOrder() {
        return ctx -> {
            StockTradeToBuyDto tradeToBuy = ctx.bodyAsClass(StockTradeToBuyDto.class);
            stockTradeService.buy(tradeToBuy);
            ctx.status(201);
        };
    }

    public Handler placeSellOrder() {
        return ctx -> {
            StockTradeToSellDto tradeToSell = ctx.bodyAsClass(StockTradeToSellDto.class);
            stockTradeService.sell(tradeToSell);
            ctx.status(201);
        };
    }

    //todo add api to get user stocks in UserPortfolioController
    //todo add api to get stocks in StockController

}