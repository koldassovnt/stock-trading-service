package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.repository.StockSellTradeQueueRepository;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;
import kz.nkoldassov.stocktrading.util.RandomUtil;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kz.nkoldassov.stocktrading.util.DateUtil.toLocalDateTime;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class StockSellTradeQueueRepositoryImpl implements StockSellTradeQueueRepository {

    private static final String INSERT_SQL = """
            insert into stock_sell_trade_queue (user_id, user_stock_id, price, updated_at)
            values (?, ?, ?, current_timestamp)
            """;

    private static final String SELECT_BY_PRICE_AND_STOCK_ID_NOT_OCCUPIED_SQL = """
            select * from stock_sell_trade_queue sstq
            left join user_stock us on us.id = sstq.user_stock_id
            left join stock s on s.id = us.stock_id
            where sstq.occupied_id is null and sstq.price = ? and s.id = ?
            order by created_at
            limit 1
            for update
            """;

    private static final String UPDATE_AS_OCCUPIED_SQL = """
            update stock_sell_trade_queue
            set occupied_id = ?, occupied_at = ?, updated_at = current_timestamp
            where id = ?
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
                stmt.setBigDecimal(3, tradeQueue.price());
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
    public Optional<StockSellTradeQueue> findAndOccupyByPriceAndStockId(BigDecimal price, Long stockId) {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            Optional<StockSellTradeQueue> queueOpt = findNotOccupiedByPriceAndStockId(price, stockId, conn);

            if (queueOpt.isPresent()) {

                StockSellTradeQueue queue = queueOpt.get();

                updateAsOccupied(conn, queue);

            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    private void updateAsOccupied(Connection conn, StockSellTradeQueue sellTrade) throws SQLException {

        try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_AS_OCCUPIED_SQL)) {

            updateStmt.setInt(1, sellTrade.occupiedId());
            updateStmt.setTimestamp(2, Timestamp.valueOf(sellTrade.occupiedAt()));
            updateStmt.setLong(3, sellTrade.id());

            updateStmt.executeUpdate();

        }

    }

    private Optional<StockSellTradeQueue> findNotOccupiedByPriceAndStockId(BigDecimal price,
                                                                           Long stockId,
                                                                           Connection conn) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PRICE_AND_STOCK_ID_NOT_OCCUPIED_SQL)) {

            stmt.setBigDecimal(1, price);
            stmt.setLong(2, stockId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToStockSellTradeQueue(rs, RandomUtil.generateOccupiedId()));
                }
            }

        }

        return Optional.empty();

    }

    private StockSellTradeQueue mapToStockSellTradeQueue(ResultSet rs, int occupiedId) throws SQLException {
        return new StockSellTradeQueue(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("user_stock_id"),
                rs.getBigDecimal("price"),
                occupiedId,
                LocalDateTime.now(),
                toLocalDateTime(rs.getTimestamp("created_at")),
                toLocalDateTime(rs.getTimestamp("updated_at"))
        );
    }

}