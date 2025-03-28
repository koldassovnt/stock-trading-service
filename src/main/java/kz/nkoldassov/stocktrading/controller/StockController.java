package kz.nkoldassov.stocktrading.controller;

import io.javalin.http.Handler;
import kz.nkoldassov.stocktrading.model.dto.StockResponseDto;
import kz.nkoldassov.stocktrading.service.StockService;

public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    public Handler loadStocks() {
        return ctx -> {
            StockResponseDto stockResponseDto = stockService.loadAll();
            ctx.json(stockResponseDto);
        };
    }

}