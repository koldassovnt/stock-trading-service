package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;

import java.util.List;

public interface StockBuyTradeQueueRepository {

    void insertAll(List<StockBuyTradeQueue> tradeQueueList);

    List<StockBuyTradeQueue> findAndOccupyByLimit(int limit);

}