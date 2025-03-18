package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.dto.StockTradeDto;

public interface StockTradeService {

    void buy(StockTradeDto stockToBuy);

    void sell(StockTradeDto stockToSell);

}