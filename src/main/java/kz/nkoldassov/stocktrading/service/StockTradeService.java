package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;

public interface StockTradeService {

    void processTradeOrder(StockBuyOrderQueue buyOrder, StockSellOrderQueue sellOrder);

}