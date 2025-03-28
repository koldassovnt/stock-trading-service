package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToSellDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockOrderService {

    void saveBuyOrderToQueue(StockOrderToBuyDto stockToBuy, Long stockId);

    void saveSellOrderToQueue(StockOrderToSellDto stockToSell);

    List<StockBuyOrderQueue> loadAndOccupyBuyOrdersByLimit(int limit);

    Optional<StockSellOrderQueue> loadAndOccupySellOrderByPriceAndStockId(BigDecimal price,
                                                                          Long stockId);

    void unOccupyBuyOrder(StockBuyOrderQueue buyOrder);

}