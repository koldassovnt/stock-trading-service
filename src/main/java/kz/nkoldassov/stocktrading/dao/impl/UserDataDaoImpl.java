package kz.nkoldassov.stocktrading.dao.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.dao.UserDataDao;
import kz.nkoldassov.stocktrading.model.db.UserData;

import javax.sql.DataSource;
import java.util.Optional;

public class UserDataDaoImpl implements UserDataDao {//todo test methods

    private static final DataSource dataSource;

    static {
        dataSource = DatabaseConfig.getDataSource();
    }

    @Override
    public Optional<UserData> loadById(Long id) {
        return Optional.empty();//todo end up
    }
}
