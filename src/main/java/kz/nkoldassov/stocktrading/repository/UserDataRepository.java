package kz.nkoldassov.stocktrading.repository;

import kz.nkoldassov.stocktrading.model.db.UserData;

import java.util.Optional;

public interface UserDataRepository {

    Optional<UserData> findById(Long id);

}