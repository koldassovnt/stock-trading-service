package kz.nkoldassov.stocktrading.mapper;

import kz.nkoldassov.stocktrading.model.db.Stock;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@SuppressWarnings("SqlDialectInspection")
public interface StockMapper {

    @Select("select * from stock where ticker = #{ticker}")
    Optional<Stock> findByTicker(String ticker);

}