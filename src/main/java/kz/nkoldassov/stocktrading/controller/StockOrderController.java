package kz.nkoldassov.stocktrading.controller;

import io.javalin.http.Handler;
import kz.nkoldassov.stocktrading.exception.InvalidBalanceAmountException;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToSellDto;
import kz.nkoldassov.stocktrading.service.StockOrderService;
import kz.nkoldassov.stocktrading.service.StockService;
import kz.nkoldassov.stocktrading.service.UserDataService;

public class StockOrderController {

    private final StockOrderService stockOrderService;
    private final UserDataService userDataService;
    private final StockService stockService;

    public StockOrderController(StockOrderService stockOrderService,
                                UserDataService userDataService,
                                StockService stockService) {
        this.stockOrderService = stockOrderService;
        this.userDataService = userDataService;
        this.stockService = stockService;
    }

    public Handler placeBuyOrder() {
        return ctx -> {
            StockOrderToBuyDto orderToBuy = ctx.bodyAsClass(StockOrderToBuyDto.class);

            if (!userDataService.canUserAfford(orderToBuy.userId(), orderToBuy.price())) {
                throw new InvalidBalanceAmountException("Provided user cannot afford to buy the stock by price");
            }

            Long stockId = stockService.loadStockIdByTicker(orderToBuy.ticker());

            stockOrderService.saveBuyOrderToQueue(orderToBuy, stockId);
            ctx.status(201);
        };
    }

    public Handler placeSellOrder() {
        return ctx -> {
            StockOrderToSellDto orderToSell = ctx.bodyAsClass(StockOrderToSellDto.class);
            stockOrderService.saveSellOrderToQueue(orderToSell);
            ctx.status(201);
        };
    }

}