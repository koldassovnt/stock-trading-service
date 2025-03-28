package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.Stock;
import kz.nkoldassov.stocktrading.model.dto.StockDto;

import java.util.List;
import java.util.Optional;

public interface StockRepository {

    Optional<Stock> findByTicker(String ticker);

    List<StockDto> findAll();

}