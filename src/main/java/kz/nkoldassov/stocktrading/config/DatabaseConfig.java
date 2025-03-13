package kz.nkoldassov.stocktrading.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(ApplicationPropsLoader.getProperty("database.url"));
        config.setUsername(ApplicationPropsLoader.getProperty("database.username"));
        config.setPassword(ApplicationPropsLoader.getProperty("database.password"));
        config.setDriverClassName(ApplicationPropsLoader.getProperty("database.driver"));
        return new HikariDataSource(config);
    }

}