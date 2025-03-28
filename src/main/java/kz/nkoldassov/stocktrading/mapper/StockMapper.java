package kz.nkoldassov.stocktrading.mapper;

import kz.nkoldassov.stocktrading.model.db.Stock;
import kz.nkoldassov.stocktrading.model.dto.StockDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SqlDialectInspection")
public interface StockMapper {

    @Select("select * from stock where ticker = #{ticker}")
    Optional<Stock> findByTicker(@Param("ticker") String ticker);

    @Select("select * from stock where id = #{id} on update")
    Optional<Stock> findStockForUpdate(@Param("id") Long id);

    @Update("update stock set current_price = #{price} where id = #{id}")
    void updateStockCurrentPrice(@Param("id") Long id,
                                 @Param("price") BigDecimal price);

    @Select("""
            select s.id,
                   s.ticker,
                   c.name as company,
                   s.current_price as currentPrice,
                   s.currency,
                   s.market_cap as marketCap,
                   s.volume,
                   ex.name as exchange
            from stock s
            left join company c on c.id = s.company_id
            left join exchange ex on ex.id = s.exchange_id
            """)
    List<StockDto> findAll();

}