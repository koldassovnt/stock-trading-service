package kz.nkoldassov.stocktrading.dao;

import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

import java.util.List;

public interface StockSellTradeQueueDao {

    void insertAll(List<StockSellTradeQueue> tradeQueueList);

}