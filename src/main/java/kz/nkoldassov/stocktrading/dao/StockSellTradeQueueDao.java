package kz.nkoldassov.stocktrading.dao;

import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockSellTradeQueueDao {

    void insertAll(List<StockSellTradeQueue> tradeQueueList);

    Optional<StockSellTradeQueue> loadAndOccupyByPrice(BigDecimal price);

}