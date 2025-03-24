package kz.nkoldassov.stocktrading.dao;

import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

public interface TradeOperationDao {

    void makeTrade(StockBuyTradeQueue buyTrade,
                   StockSellTradeQueue sellTrade);

}