package kz.nkoldassov.stocktrading.repository.impl;

import kz.nkoldassov.stocktrading.config.MyBatisConfig;
import kz.nkoldassov.stocktrading.mapper.StockMapper;
import kz.nkoldassov.stocktrading.mapper.UserDataMapper;
import kz.nkoldassov.stocktrading.mapper.UserStockMapper;
import kz.nkoldassov.stocktrading.model.db.*;
import kz.nkoldassov.stocktrading.repository.StockTradeOperationRepository;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class StockTradeOperationRepositoryImpl implements StockTradeOperationRepository {

    @Override
    public void makeStockTradeOperation(StockBuyOrderQueue buyOrder, StockSellOrderQueue sellOrder) {

        try (SqlSession session = MyBatisConfig.getSession()) {
            Connection conn = session.getConnection();
            conn.setAutoCommit(false);

            try {

                UserStockMapper userStockMapper = session.getMapper(UserStockMapper.class);
                UserDataMapper userDataMapper = session.getMapper(UserDataMapper.class);
                StockMapper stockMapper = session.getMapper(StockMapper.class);

                updateUserStockHolder(buyOrder, sellOrder, userStockMapper);

                updateBuyerBalance(buyOrder, userDataMapper);

                updateSellerBalance(buyOrder, sellOrder, userDataMapper);

                updateStockCurrentPrice(buyOrder, stockMapper);

                conn.commit();

            } catch (Exception ex) {
                conn.rollback();
                throw new RuntimeException("Transaction failed", ex);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void updateStockCurrentPrice(StockBuyOrderQueue buyOrder, StockMapper stockMapper) {

        Optional<Stock> stockOpt = stockMapper.findStockForUpdate(buyOrder.stockId());

        if (stockOpt.isEmpty()) {
            throw new RuntimeException("No stock by id = " + buyOrder.stockId());
        }

        stockMapper.updateStockCurrentPrice(buyOrder.stockId(), buyOrder.price());

    }

    private static void updateSellerBalance(StockBuyOrderQueue buyOrder,
                                            StockSellOrderQueue sellOrder,
                                            UserDataMapper userDataMapper) {

        Optional<UserData> sellerOpt = userDataMapper.findByIdForUpdate(sellOrder.userId());

        if (sellerOpt.isEmpty()) {
            throw new RuntimeException("Not found seller by id = " + sellOrder.userId());
        }

        UserData seller = sellerOpt.get();
        userDataMapper.updateBalance(seller.id(), seller.balanceAmount().add(buyOrder.price()));

    }

    private static void updateBuyerBalance(StockBuyOrderQueue buyOrder, UserDataMapper userDataMapper) {

        Optional<UserData> buyerOpt = userDataMapper.findByIdForUpdate(buyOrder.userId());

        if (buyerOpt.isEmpty()) {
            throw new RuntimeException("Not found buyer by id = " + buyOrder.userId());
        }

        UserData buyer = buyerOpt.get();

        if (buyer.balanceAmount().compareTo(buyOrder.price()) < 0) {
            throw new RuntimeException("Buyer does not have enough balance");
        }

        userDataMapper.updateBalance(buyer.id(), buyer.balanceAmount().subtract(buyOrder.price()));

    }

    private static void updateUserStockHolder(StockBuyOrderQueue buyOrder,
                                              StockSellOrderQueue sellOrder,
                                              UserStockMapper userStockMapper) {

        Optional<UserStock> userStockOpt = userStockMapper.findUserStockForUpdate(sellOrder.userStockId());

        if (userStockOpt.isEmpty()) {
            throw new RuntimeException("Not found userStock by id = " + sellOrder.userStockId());
        }

        userStockMapper.updateStockHolder(buyOrder.userId(), buyOrder.price(), sellOrder.userStockId());

    }

}