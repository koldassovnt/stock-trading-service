package kz.nkoldassov.stocktrading.dao.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.dao.TradeOperationDao;
import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class TradeOperationDaoImpl implements TradeOperationDao {//todo test methods

    private static final String UPDATE_USER_STOCK_SQL = """
            update user_stock
            set user_id = ?, price = ?
            where id = ?
            """;

    private static final DataSource dataSource;

    static {
        dataSource = DatabaseConfig.getDataSource();
    }

    @Override
    public void makeTrade(StockBuyTradeQueue buyTrade, StockSellTradeQueue sellTrade) {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_STOCK_SQL)) {

                stmt.setLong(1, buyTrade.userId());
                stmt.setBigDecimal(2, sellTrade.price());
                stmt.setLong(3, sellTrade.userStockId());

                stmt.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}