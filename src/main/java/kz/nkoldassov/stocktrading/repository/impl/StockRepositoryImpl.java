package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.mapper.StockMapper;
import kz.nkoldassov.stocktrading.model.db.Stock;
import kz.nkoldassov.stocktrading.config.MyBatisRepository;
import kz.nkoldassov.stocktrading.model.dto.StockDto;
import kz.nkoldassov.stocktrading.repository.StockRepository;

import java.util.List;
import java.util.Optional;

public class StockRepositoryImpl implements StockRepository {

    @Override
    public Optional<Stock> findByTicker(String ticker) {
        return MyBatisRepository.execute(session -> {
            StockMapper stockMapper = session.getMapper(StockMapper.class);
            return stockMapper.findByTicker(ticker);
        });
    }

    @Override
    public List<StockDto> findAll() {
        return MyBatisRepository.execute(session -> {
          StockMapper stockMapper = session.getMapper(StockMapper.class);
          return stockMapper.findAll();
        });
    }

}