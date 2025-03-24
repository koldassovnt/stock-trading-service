package kz.nkoldassov.stocktrading.mapper;

import kz.nkoldassov.stocktrading.model.db.UserData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Optional;

@SuppressWarnings("SqlDialectInspection")
public interface UserDataMapper {

    @Select("select * from user_data where id = #{id}")
    Optional<UserData> findById(@Param("id") Long id);

    @Select("select * from user_data where id = #{id} for update")
    Optional<UserData> findByIdForUpdate(@Param("id") Long id);

    @Update("update user_data set balance = #{newBalance} where id = #{id}")
    void updateBalance(@Param("id") Long id, @Param("newBalance") BigDecimal newBalance);

}