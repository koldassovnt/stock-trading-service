package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.MyBatisRepository;
import kz.nkoldassov.stocktrading.mapper.UserStockMapper;
import kz.nkoldassov.stocktrading.model.dto.UserPortfolioDto;
import kz.nkoldassov.stocktrading.repository.UserStockRepository;

import java.util.List;

public class UserStockRepositoryImpl implements UserStockRepository {

    @Override
    public List<UserPortfolioDto.Detail> findDetailsByUserId(Long userId) {
        return MyBatisRepository.execute(session -> {
            UserStockMapper userStockMapper = session.getMapper(UserStockMapper.class);
            return userStockMapper.findDetailsByUserId(userId);
        });
    }

}