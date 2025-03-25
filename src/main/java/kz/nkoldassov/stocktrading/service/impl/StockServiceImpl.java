package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.exception.NoStockException;
import kz.nkoldassov.stocktrading.model.db.Stock;
import kz.nkoldassov.stocktrading.repository.StockRepository;
import kz.nkoldassov.stocktrading.service.StockService;
import org.apache.commons.lang3.StringUtils;

public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Long loadStockIdByTicker(String ticker) {

        if (StringUtils.isBlank(ticker)) {
            throw new IllegalArgumentException("Ticker cannot be null or blank");
        }

        return stockRepository.findByTicker(ticker)
                .map(Stock::id)
                .orElseThrow(() -> new NoStockException("No stock by ticker = " + ticker));

    }

}