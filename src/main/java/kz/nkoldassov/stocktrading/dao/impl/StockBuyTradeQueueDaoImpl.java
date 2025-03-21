package kz.nkoldassov.stocktrading.dao.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.dao.StockBuyTradeQueueDao;
import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kz.nkoldassov.stocktrading.util.DateUtil.toLocalDateTime;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class StockBuyTradeQueueDaoImpl implements StockBuyTradeQueueDao {

    private static final String INSERT_SQL = """
            insert into stock_buy_trade_queue (user_id, price, updated_at)
            values (?, ?, current_timestamp)
            """;

    private static final String SELECT_NOT_OCCUPIED_SQL = """
            select * from stock_buy_trade_queue
            where occupied_id is null
            order by created_at
            limit ?
            for update
            """;

    private static final String UPDATE_SQL = """
            update stock_buy_trade_queue
            set occupied_id = ?, occupied_at = ?, updated_at = current_timestamp
            where id = ?
            """;

    private static final DataSource dataSource;

    static {
        dataSource = DatabaseConfig.getDataSource();
    }

    @Override
    public void insertAll(List<StockBuyTradeQueue> tradeQueueList) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);

            for (StockBuyTradeQueue tradeQueue : tradeQueueList) {
                stmt.setLong(1, tradeQueue.userId());
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

    @Override
    public List<StockBuyTradeQueue> loadAndOccupy(int limit, int occupiedId) {

        List<StockBuyTradeQueue> tradeQueue = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(SELECT_NOT_OCCUPIED_SQL)) {

                stmt.setInt(1, limit);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        StockBuyTradeQueue queueItem = mapToTradeQueue(rs, occupiedId);
                        tradeQueue.add(queueItem);
                    }
                }

            }

            try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_SQL)) {

                for (StockBuyTradeQueue queueItem : tradeQueue) {
                    updateStmt.setInt(1, queueItem.occupiedId());
                    updateStmt.setTimestamp(2, Timestamp.valueOf(queueItem.occupiedAt()));
                    updateStmt.setLong(3, queueItem.id());
                    updateStmt.addBatch();
                }

                updateStmt.executeBatch();

            }

            conn.commit();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tradeQueue;

    }

    private StockBuyTradeQueue mapToTradeQueue(ResultSet rs, int occupiedId) throws SQLException {
        return new StockBuyTradeQueue(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("price"),
                occupiedId,
                LocalDateTime.now(),
                toLocalDateTime(rs.getTimestamp("created_at")),
                toLocalDateTime(rs.getTimestamp("updated_at"))
        );
    }

}