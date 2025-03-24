package kz.nkoldassov.stocktrading.repository;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserStockRepository {

    boolean updateUserStockHolder(@Param("userId") Long userId,
                                  @Param("price") BigDecimal price,
                                  @Param("id") Long id);

}