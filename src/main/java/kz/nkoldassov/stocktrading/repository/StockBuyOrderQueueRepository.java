package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;

import java.time.LocalDateTime;
import java.util.List;

public interface StockBuyOrderQueueRepository {

    void insertAll(List<StockBuyOrderQueue> buyOrderList);

    List<StockBuyOrderQueue> findAndOccupyByLimit(int limit);

    void updateOccupied(Long id, Integer occupiedId, LocalDateTime occupiedAt);

}