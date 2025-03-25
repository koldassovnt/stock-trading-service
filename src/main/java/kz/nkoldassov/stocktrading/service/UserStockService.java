package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.dto.UserPortfolioDto;

public interface UserStockService {

    UserPortfolioDto loadPortfolio(Long userId);

}