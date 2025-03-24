package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.Stock;

import java.util.Optional;

public interface StockRepository {

    Optional<Stock> findByTicker(String ticker);

}