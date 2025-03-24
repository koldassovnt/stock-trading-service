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
    public void makeStockTradeOperation(StockBuyTradeQueue buyTrade, StockSellTradeQueue sellTrade) {

        try (SqlSession session = MyBatisConfig.getSession()) {
            Connection conn = session.getConnection();
            conn.setAutoCommit(false);

            try {

                UserStockMapper userStockMapper = session.getMapper(UserStockMapper.class);
                UserDataMapper userDataMapper = session.getMapper(UserDataMapper.class);
                StockMapper stockMapper = session.getMapper(StockMapper.class);

                updateUserStockHolder(buyTrade, sellTrade, userStockMapper);

                updateBuyerBalance(buyTrade, userDataMapper);

                updateSellerBalance(buyTrade, sellTrade, userDataMapper);

                updateStockCurrentPrice(buyTrade, stockMapper);

                conn.commit();

            } catch (Exception ex) {
                conn.rollback();
                throw new RuntimeException("Transaction failed", ex);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void updateStockCurrentPrice(StockBuyTradeQueue buyTrade, StockMapper stockMapper) {

        Optional<Stock> stockOpt = stockMapper.findStockForUpdate(buyTrade.stockId());

        if (stockOpt.isEmpty()) {
            throw new RuntimeException("No stock by id = " + buyTrade.stockId());
        }

        stockMapper.updateStockCurrentPrice(buyTrade.stockId(), buyTrade.price());

    }

    private static void updateSellerBalance(StockBuyTradeQueue buyTrade,
                                            StockSellTradeQueue sellTrade,
                                            UserDataMapper userDataMapper) {

        Optional<UserData> sellerOpt = userDataMapper.findByIdForUpdate(sellTrade.userId());

        if (sellerOpt.isEmpty()) {
            throw new RuntimeException("Not found seller by id = " + sellTrade.userId());
        }

        UserData seller = sellerOpt.get();
        userDataMapper.updateBalance(seller.id(), seller.balanceAmount().add(buyTrade.price()));

    }

    private static void updateBuyerBalance(StockBuyTradeQueue buyTrade, UserDataMapper userDataMapper) {

        Optional<UserData> buyerOpt = userDataMapper.findByIdForUpdate(buyTrade.userId());

        if (buyerOpt.isEmpty()) {
            throw new RuntimeException("Not found buyer by id = " + buyTrade.userId());
        }

        UserData buyer = buyerOpt.get();

        if (buyer.balanceAmount().compareTo(buyTrade.price()) < 0) {
            throw new RuntimeException("Buyer does not have enough balance");
        }

        userDataMapper.updateBalance(buyer.id(), buyer.balanceAmount().subtract(buyTrade.price()));

    }

    private static void updateUserStockHolder(StockBuyTradeQueue buyTrade,
                                              StockSellTradeQueue sellTrade,
                                              UserStockMapper userStockMapper) {

        Optional<UserStock> userStockOpt = userStockMapper.findUserStockForUpdate(sellTrade.userStockId());

        if (userStockOpt.isEmpty()) {
            throw new RuntimeException("Not found userStock by id = " + sellTrade.userStockId());
        }

        userStockMapper.updateStockHolder(buyTrade.userId(), buyTrade.price(), sellTrade.userStockId());

    }

}