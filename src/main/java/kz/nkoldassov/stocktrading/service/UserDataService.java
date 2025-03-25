package kz.nkoldassov.stocktrading.service;

import java.math.BigDecimal;

public interface UserDataService {

    boolean canUserAfford(Long userId, BigDecimal priceToBuy);

}