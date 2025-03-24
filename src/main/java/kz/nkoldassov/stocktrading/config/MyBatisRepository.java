package kz.nkoldassov.stocktrading.config;

import org.apache.ibatis.session.SqlSession;

import java.util.function.Function;

public abstract class MyBatisRepository {

    public static <R> R execute(Function<SqlSession, R> function) {
        try (SqlSession session = MyBatisConfig.getSession()) {
            return function.apply(session);
        }
    }

    public static void executeVoid(Function<SqlSession, Void> function) {
        try (SqlSession session = MyBatisConfig.getSession()) {
            function.apply(session);
        }
    }

}