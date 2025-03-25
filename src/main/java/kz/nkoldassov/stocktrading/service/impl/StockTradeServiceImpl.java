package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;
import kz.nkoldassov.stocktrading.repository.StockTradeOperationRepository;
import kz.nkoldassov.stocktrading.service.StockTradeService;

public class StockTradeServiceImpl implements StockTradeService {

    private final StockTradeOperationRepository stockTradeOperationRepository;

    public StockTradeServiceImpl(StockTradeOperationRepository stockTradeOperationRepository) {
        this.stockTradeOperationRepository = stockTradeOperationRepository;
    }

    @Override
    public void processTradeOrder(StockBuyOrderQueue buyOrder, StockSellOrderQueue sellOrder) {
        stockTradeOperationRepository.makeStockTradeOperation(buyOrder, sellOrder);
    }

}