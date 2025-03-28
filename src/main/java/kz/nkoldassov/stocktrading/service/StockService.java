package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.dto.StockResponseDto;

public interface StockService {

    Long loadStockIdByTicker(String ticker);

    StockResponseDto loadAll();

}