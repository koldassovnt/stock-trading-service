package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.MyBatisRepository;
import kz.nkoldassov.stocktrading.mapper.UserStockMapper;
import kz.nkoldassov.stocktrading.repository.UserStockRepository;

import java.math.BigDecimal;

public class UserStockRepositoryImpl implements UserStockRepository {

    @Override
    public boolean updateUserStockHolder(Long userId, BigDecimal price, Long id) {
        return MyBatisRepository.execute(session -> {
            UserStockMapper userStockMapper = session.getMapper(UserStockMapper.class);
            int affectedRows = userStockMapper.updateUserStockHolder(userId, price, id);
            session.commit();
            return affectedRows > 0;
        });
    }

}