package kz.nkoldassov.stocktrading.config;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

public class MyBatisConfig {

    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        try {

            DataSource dataSource = DatabaseConfig.getDataSource();

            Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);

            Configuration configuration = new Configuration();
            configuration.setEnvironment(environment);

            configuration.addMappers("kz.nkoldassov.stocktrading.mapper");

            SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(configuration);

        } catch (Exception e) {
            throw new RuntimeException("Error initializing MyBatis", e);
        }
    }

    public static SqlSession getSession() {
        return SQL_SESSION_FACTORY.openSession();
    }

}