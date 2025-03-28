package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.dto.UserPortfolioDto;

import java.util.List;

public interface UserStockRepository {

    List<UserPortfolioDto.Detail> findDetailsByUserId(Long userId);

}