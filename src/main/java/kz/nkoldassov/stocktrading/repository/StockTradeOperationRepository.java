package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;

public interface StockTradeOperationRepository {

    void makeStockTradeOperation(StockBuyOrderQueue buyOrder, StockSellOrderQueue sellOrder);

}