package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

public interface StockTradeOperationRepository {

    void makeStockTradeOperation(StockBuyTradeQueue buyTrade, StockSellTradeQueue sellTrade);

}