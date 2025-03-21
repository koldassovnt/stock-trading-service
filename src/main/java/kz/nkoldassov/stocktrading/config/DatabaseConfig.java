package kz.nkoldassov.stocktrading.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    private static final HikariDataSource DATA_SOURCE;

    static  {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(ApplicationPropsLoader.getProperty("database.url"));
        config.setUsername(ApplicationPropsLoader.getProperty("database.username"));
        config.setPassword(ApplicationPropsLoader.getProperty("database.password"));
        config.setDriverClassName(ApplicationPropsLoader.getProperty("database.driver"));
        config.setMaximumPoolSize(getMaxPoolSize());

        DATA_SOURCE = new HikariDataSource(config);
    }

    private static int getMaxPoolSize() {
        return Integer.parseInt(ApplicationPropsLoader.getProperty("database.maxPoolSize"));
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

}