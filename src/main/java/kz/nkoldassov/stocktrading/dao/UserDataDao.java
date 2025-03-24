package kz.nkoldassov.stocktrading.dao;

import kz.nkoldassov.stocktrading.model.db.UserData;

import java.util.Optional;

public interface UserDataDao {

    Optional<UserData> loadById(Long id);

}