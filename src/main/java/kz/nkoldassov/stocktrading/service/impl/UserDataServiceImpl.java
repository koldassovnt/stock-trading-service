package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.exception.NoUserDataException;
import kz.nkoldassov.stocktrading.model.db.UserData;
import kz.nkoldassov.stocktrading.repository.UserDataRepository;
import kz.nkoldassov.stocktrading.service.UserDataService;

import java.math.BigDecimal;
import java.util.Optional;

public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;

    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public boolean canUserAfford(Long userId, BigDecimal priceToBuy) {

        Optional<UserData> userDataOpt = userDataRepository.findById(userId);

        if (userDataOpt.isEmpty()) {
            throw new NoUserDataException("No userData by id = " + userId);
        }

        UserData userData = userDataOpt.get();

        return userData.balanceAmount().compareTo(priceToBuy) > 0;

    }

}