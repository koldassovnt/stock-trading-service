package kz.nkoldassov.stocktrading.mapper;

import kz.nkoldassov.stocktrading.model.db.UserStock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Optional;

@SuppressWarnings("SqlDialectInspection")
public interface UserStockMapper {

    @Update("""
            update user_stock
            set user_id = #{userId}, price = #{price}
            where id = #{id}
            """)
    void updateStockHolder(@Param("userId") Long userId,
                           @Param("price") BigDecimal price,
                           @Param("id") Long id);

    @Select("select * from user_stock where id = #{id} for update")
    Optional<UserStock> findUserStockForUpdate(@Param("id") Long id);

}