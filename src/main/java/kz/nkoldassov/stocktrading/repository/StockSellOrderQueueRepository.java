package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockSellOrderQueueRepository {

    void insertAll(List<StockSellOrderQueue> sellOrderList);

    Optional<StockSellOrderQueue> findAndOccupyByPriceAndStockId(BigDecimal price, Long stockId);

}