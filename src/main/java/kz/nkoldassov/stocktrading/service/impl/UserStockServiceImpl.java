package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.model.dto.UserPortfolioDto;
import kz.nkoldassov.stocktrading.repository.UserStockRepository;
import kz.nkoldassov.stocktrading.service.UserStockService;

import java.util.List;

public class UserStockServiceImpl implements UserStockService {

    private final UserStockRepository userStockRepository;

    public UserStockServiceImpl(UserStockRepository userStockRepository) {
        this.userStockRepository = userStockRepository;
    }

    @Override
    public UserPortfolioDto loadPortfolio(Long userId) {
        List<UserPortfolioDto.Detail> userStockDetails = userStockRepository.findDetailsByUserId(userId);
        return UserPortfolioDto.of(userId, userStockDetails);
    }

}