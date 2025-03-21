package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.dao.StockBuyTradeQueueDao;
import kz.nkoldassov.stocktrading.dao.StockSellTradeQueueDao;
import kz.nkoldassov.stocktrading.model.db.StockBuyTradeQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellTradeQueue;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToSellDto;
import kz.nkoldassov.stocktrading.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StockTradeServiceImpl implements StockTradeService {//todo test methods

    private static final Logger logger = LoggerFactory.getLogger(StockTradeServiceImpl.class);
    private static final int PROCESS_TRADES_DEFAULT_LIMIT = 10;

    private final StockBuyTradeQueueDao stockBuyTradeQueueDao;
    private final StockSellTradeQueueDao stockSellTradeQueueDao;

    public StockTradeServiceImpl(StockBuyTradeQueueDao stockBuyTradeQueueDao,
                                 StockSellTradeQueueDao stockSellTradeQueueDao) {
        this.stockBuyTradeQueueDao = stockBuyTradeQueueDao;
        this.stockSellTradeQueueDao = stockSellTradeQueueDao;
    }

    @Override
    public void buy(StockTradeToBuyDto stockToBuy) {

        List<StockBuyTradeQueue> tradeQueueList = IntStream.range(0, stockToBuy.quantity())
                .mapToObj(i -> new StockBuyTradeQueue(
                        null,
                        stockToBuy.userId(),
                        stockToBuy.price(),
                        null,
                        null,
                        null,
                        null
                )).collect(Collectors.toList());

        stockBuyTradeQueueDao.insertAll(tradeQueueList);

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

        stockSellTradeQueueDao.insertAll(tradeQueueList);

    }

    @Override
    public void processTrades() {

        int limit = getLimit();

        //todo load limited to buy operations and occupy them
        List<StockBuyTradeQueue> toBuyOperations = stockBuyTradeQueueDao.loadAndOccupy(
                limit,
                RandomUtil.generateOccupiedId());

        for (StockBuyTradeQueue toBuyOperation : toBuyOperations) {

            //todo take nth to buy in loop and try to find him to sell operation (select on update not occupied)
            //todo if found match then in one transaction:
            //todo      transfer stock from one user_stock to another,
            //todo      make buy operation finished
            //todo      make sell operation finished

            //todo if not found make occupied null again

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