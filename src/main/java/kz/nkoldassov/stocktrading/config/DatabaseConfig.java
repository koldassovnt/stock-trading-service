package kz.nkoldassov.stocktrading.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {

        try {
            properties.load(DatabaseConfig.class.getClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties", e);
        }

    }

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("database.url"));
        config.setUsername(properties.getProperty("database.username"));
        config.setPassword(properties.getProperty("database.password"));
        config.setDriverClassName(properties.getProperty("database.driver"));
        return new HikariDataSource(config);
    }
}
