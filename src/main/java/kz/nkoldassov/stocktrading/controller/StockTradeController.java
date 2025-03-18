package kz.nkoldassov.stocktrading.controller;

import io.javalin.http.Handler;
import kz.nkoldassov.stocktrading.model.dto.StockTradeDto;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import kz.nkoldassov.stocktrading.service.StockTradeServiceImpl;

public class StockTradeController {

    private StockTradeController() {}

    public static Handler placeBuyOrder = ctx -> {

        StockTradeDto trade = ctx.bodyAsClass(StockTradeDto.class);

        StockTradeService stockTradeService = StockTradeServiceImpl.instance();

        stockTradeService.buy(trade);

        ctx.status(201);

    };

    public static Handler placeSellOrder = ctx -> {

        StockTradeDto trade = ctx.bodyAsClass(StockTradeDto.class);

        StockTradeService stockTradeService = StockTradeServiceImpl.instance();

        stockTradeService.sell(trade);

        ctx.status(201);

    };

}