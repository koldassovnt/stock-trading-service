package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.DatabaseConfig;
import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.repository.StockBuyOrderQueueRepository;
import kz.nkoldassov.stocktrading.util.RandomUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kz.nkoldassov.stocktrading.util.DateUtil.toLocalDateTime;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class StockBuyOrderQueueRepositoryImpl implements StockBuyOrderQueueRepository {

    private static final String INSERT_SQL = """
            insert into stock_buy_order_queue (user_id, price, stock_id updated_at)
            values (?, ?, current_timestamp)
            """;

    private static final String SELECT_NOT_OCCUPIED_SQL = """
            select * from stock_buy_order_queue
            where occupied_id is null
            order by created_at
            limit ?
            for update
            """;

    private static final String UPDATE_AS_OCCUPIED_SQL = """
            update stock_buy_order_queue
            set occupied_id = ?, occupied_at = ?, updated_at = current_timestamp
            where id = ?
            """;

    private static final DataSource dataSource;

    static {
        dataSource = DatabaseConfig.getDataSource();
    }

    @Override
    public void insertAll(List<StockBuyOrderQueue> buyOrderList) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);

            for (StockBuyOrderQueue buyOrder : buyOrderList) {
                stmt.setLong(1, buyOrder.userId());
                stmt.setBigDecimal(2, buyOrder.price());
                stmt.setLong(3, buyOrder.stockId());
                stmt.addBatch();
            }

            int[] affectedRows = stmt.executeBatch();
            conn.commit();

            if (Arrays.stream(affectedRows).sum() != buyOrderList.size()) {
                throw new SQLException("Batch insert failed: Not all rows were inserted.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<StockBuyOrderQueue> findAndOccupyByLimit(int limit) {

        List<StockBuyOrderQueue> buyOrderList;

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            buyOrderList = findNotOccupied(limit, conn);

            updateAsOccupied(conn, buyOrderList);

            conn.commit();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buyOrderList;

    }

    @Override
    public void updateOccupied(Long id, Integer occupiedId, LocalDateTime occupiedAt) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(UPDATE_AS_OCCUPIED_SQL)) {

            updateStmt.setInt(1, occupiedId);

            if (occupiedAt != null) {
                updateStmt.setTimestamp(2, Timestamp.valueOf(occupiedAt));
            } else {
                updateStmt.setTimestamp(2, null);
            }

            updateStmt.setLong(3, id);

            updateStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateAsOccupied(Connection conn, List<StockBuyOrderQueue> buyOrderList) throws SQLException {

        try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_AS_OCCUPIED_SQL)) {

            for (StockBuyOrderQueue buyOrder : buyOrderList) {
                updateStmt.setInt(1, buyOrder.occupiedId());
                updateStmt.setTimestamp(2, Timestamp.valueOf(buyOrder.occupiedAt()));
                updateStmt.setLong(3, buyOrder.id());
                updateStmt.addBatch();
            }

            updateStmt.executeBatch();

        }

    }

    private List<StockBuyOrderQueue> findNotOccupied(int limit, Connection conn) throws SQLException {

        List<StockBuyOrderQueue> buyOrderList = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_NOT_OCCUPIED_SQL)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StockBuyOrderQueue buyOrder = mapToBuyOrderQueue(rs);
                    buyOrderList.add(buyOrder);
                }
            }

        }

        return buyOrderList;

    }

    private StockBuyOrderQueue mapToBuyOrderQueue(ResultSet rs) throws SQLException {
        return new StockBuyOrderQueue(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("price"),
                rs.getLong("stock_id"),
                RandomUtil.generateOccupiedId(),
                LocalDateTime.now(),
                toLocalDateTime(rs.getTimestamp("created_at")),
                toLocalDateTime(rs.getTimestamp("updated_at"))
        );
    }

}