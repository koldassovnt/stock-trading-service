package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.dto.StockTradeToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToSellDto;

public interface StockTradeService {

    void buy(StockTradeToBuyDto stockToBuy);

    void sell(StockTradeToSellDto stockToSell);

    void processTrades();

}