package kz.nkoldassov.stocktrading.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@SuppressWarnings("SqlDialectInspection")
public interface UserStockMapper {

    @Update("""
            update user_stock
            set user_id = #{userId}, price = #{price}
            where id = #{id}
            """)
    int updateUserStockHolder(@Param("userId") Long userId,
                              @Param("price") BigDecimal price,
                              @Param("id") Long id);

}