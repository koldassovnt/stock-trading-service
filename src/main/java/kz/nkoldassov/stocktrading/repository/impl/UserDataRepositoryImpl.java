package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.MyBatisRepository;
import kz.nkoldassov.stocktrading.mapper.UserDataMapper;
import kz.nkoldassov.stocktrading.model.db.UserData;
import kz.nkoldassov.stocktrading.repository.UserDataRepository;

import java.util.Optional;

public class UserDataRepositoryImpl implements UserDataRepository {

    @Override
    public Optional<UserData> findById(Long id) {
        return MyBatisRepository.execute(session -> {
            UserDataMapper userDataMapper = session.getMapper(UserDataMapper.class);
            return userDataMapper.findById(id);
        });
    }

}