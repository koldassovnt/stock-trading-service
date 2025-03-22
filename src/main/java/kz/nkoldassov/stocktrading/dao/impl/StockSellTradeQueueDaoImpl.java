package kz.nkoldassov.stocktrading.dao.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.dao.StockSellTradeQueueDao;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class StockSellTradeQueueDaoImpl implements StockSellTradeQueueDao {

    private static final String INSERT_SQL = """
            insert into stock_sell_trade_queue (user_id, user_stock_id, price, updated_at)
            values (?, ?, ?, current_timestamp)
            """;

    private static final DataSource dataSource;

    static {
        dataSource = DatabaseConfig.getDataSource();
    }

    @Override
    public void insertAll(List<StockSellTradeQueue> tradeQueueList) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);

            for (StockSellTradeQueue tradeQueue : tradeQueueList) {
                stmt.setLong(1, tradeQueue.userId());
                stmt.setLong(2, tradeQueue.userStockId());
                stmt.setBigDecimal(2, tradeQueue.price());
                stmt.addBatch();
            }

            int[] affectedRows = stmt.executeBatch();
            conn.commit();

            if (Arrays.stream(affectedRows).sum() != tradeQueueList.size()) {
                throw new SQLException("Batch insert failed: Not all rows were inserted.");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}