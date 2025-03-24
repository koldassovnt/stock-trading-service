package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.exception.InvalidBalanceAmountException;
import kz.nkoldassov.stocktrading.exception.NoStockException;
import kz.nkoldassov.stocktrading.exception.NoUserDataException;
import kz.nkoldassov.stocktrading.model.db.Stock;
import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;
import kz.nkoldassov.stocktrading.model.db.UserData;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToSellDto;
import kz.nkoldassov.stocktrading.repository.*;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StockTradeServiceImpl implements StockTradeService {//todo test methods

    private static final Logger logger = LoggerFactory.getLogger(StockTradeServiceImpl.class);
    private static final int PROCESS_TRADES_DEFAULT_LIMIT = 10;

    private final StockBuyTradeQueueRepository stockBuyTradeQueueRepository;
    private final StockSellTradeQueueRepository stockSellTradeQueueRepository;
    private final UserStockRepository userStockRepository;
    private final UserDataRepository userDataRepository;
    private final StockRepository stockRepository;

    public StockTradeServiceImpl(StockBuyTradeQueueRepository stockBuyTradeQueueRepository,
                                 StockSellTradeQueueRepository stockSellTradeQueueRepository,
                                 UserStockRepository userStockRepository,
                                 UserDataRepository userDataRepository,
                                 StockRepository stockRepository) {
        this.stockBuyTradeQueueRepository = stockBuyTradeQueueRepository;
        this.stockSellTradeQueueRepository = stockSellTradeQueueRepository;
        this.userStockRepository = userStockRepository;
        this.userDataRepository = userDataRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public void buy(StockTradeToBuyDto stockToBuy) {

        Optional<UserData> userDataOpt = userDataRepository.findById(stockToBuy.userId());

        if (userDataOpt.isEmpty()) {
            throw new NoUserDataException("No userData by id = " + stockToBuy.userId());
        }

        UserData userData = userDataOpt.get();

        if (userData.balanceAmount().compareTo(stockToBuy.price()) < 0) {
            throw new InvalidBalanceAmountException("Provided user balance is less than requested to by price");
        }

        Optional<Stock> stockOpt = stockRepository.findByTicker(stockToBuy.ticker());

        if (stockOpt.isEmpty()) {
            throw new NoStockException("No stock by ticker = " + stockToBuy.ticker());
        }

        Long stockId = stockOpt.get().id();

        List<StockBuyTradeQueue> tradeQueueList = IntStream.range(0, stockToBuy.quantity())
                .mapToObj(i -> new StockBuyTradeQueue(
                        null,
                        stockToBuy.userId(),
                        stockToBuy.price(),
                        stockId,
                        null,
                        null,
                        null,
                        null
                )).collect(Collectors.toList());

        stockBuyTradeQueueRepository.insertAll(tradeQueueList);

    }

    @Override
    public void sell(StockTradeToSellDto stockToSell) {

        List<StockSellTradeQueue> tradeQueueList = stockToSell.stockTradeDetail()
                .stream()
                .map(detail -> new StockSellTradeQueue(
                        null,
                        stockToSell.userId(),
                        detail.userStockId(),
                        detail.price(),
                        null,
                        null,
                        null,
                        null
                )).toList();

        stockSellTradeQueueRepository.insertAll(tradeQueueList);

    }

    @Override
    public void processTrades() {//todo slice diff logic

        int limit = getLimit();

        List<StockBuyTradeQueue> toBuyOperations = stockBuyTradeQueueRepository.findAndOccupyByLimit(limit);

        for (StockBuyTradeQueue buyTrade : toBuyOperations) {

            Optional<StockSellTradeQueue> sellTradeOpt =
                    stockSellTradeQueueRepository.findAndOccupyByPriceAndStockId(
                            buyTrade.price(),
                            buyTrade.stockId());

            if (sellTradeOpt.isPresent()) {

                StockSellTradeQueue sellTrade = sellTradeOpt.get();

                boolean isExecuted = userStockRepository.updateUserStockHolder(
                        buyTrade.userId(),
                        sellTrade.price(),
                        sellTrade.userStockId());

                if (isExecuted) {
                    //todo transfer price to seller balanceAmount
                    //todo update stock currentPrice
                } else {
                    logger.error("2AqFkoMq :: user_stock transfer " +
                            "by id = {} " +
                            "from user = {} " +
                            "to user = {} was not executed",
                            sellTrade.userStockId(), sellTrade.userId(), buyTrade.userId());
                    //todo make occupied as null
                }

            }

        }

    }

    private int getLimit() {
        try {
            return Integer.parseInt(ApplicationPropsLoader.getProperty("trade.queue.limit"));
        } catch (Exception ex) {
            logger.error("YwJh3f7K :: error in getting property trade.queue.limit");
            return PROCESS_TRADES_DEFAULT_LIMIT;
        }
    }

}