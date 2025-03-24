package kz.nkoldassov.stocktrading.mapper;

import kz.nkoldassov.stocktrading.model.db.UserData;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@SuppressWarnings("SqlDialectInspection")
public interface UserDataMapper {

    @Select("select * from user_data where id = #{id}")
    Optional<UserData> findById(Long id);

}