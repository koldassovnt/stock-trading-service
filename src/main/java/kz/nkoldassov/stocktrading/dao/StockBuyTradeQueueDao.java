package kz.nkoldassov.stocktrading.dao;

import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;

import java.util.List;

public interface StockBuyTradeQueueDao {

    void insertAll(List<StockBuyTradeQueue> tradeQueueList);

    List<StockBuyTradeQueue> loadAndOccupy(int limit);

}