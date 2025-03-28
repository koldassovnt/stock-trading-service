package kz.nkoldassov.stocktrading.scheduler;

import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;
import kz.nkoldassov.stocktrading.service.StockOrderService;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockTradeScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockTradeScheduler.class);
    private static final int PROCESS_ORDERS_DEFAULT_LIMIT = 10;

    private final StockTradeService stockTradeService;
    private final StockOrderService stockOrderService;

    public StockTradeScheduler(StockTradeService stockTradeService,
                               StockOrderService stockOrderService) {
        this.stockTradeService = stockTradeService;
        this.stockOrderService = stockOrderService;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        scheduler.scheduleAtFixedRate(this::processOrders, 0, 2, TimeUnit.MINUTES);
    }

    private void processOrders() {
        logger.info("MxsCk8fT :: Processing operations at: {}", LocalDateTime.now());

        try {
            int limit = getLimit();

            List<StockBuyOrderQueue> stockBuyOrderQueues = stockOrderService.loadAndOccupyBuyOrdersByLimit(limit);

            for (StockBuyOrderQueue buyOrder : stockBuyOrderQueues) {

                Optional<StockSellOrderQueue> sellOrderOpt = stockOrderService.loadAndOccupySellOrderByPriceAndStockId(
                        buyOrder.price(),
                        buyOrder.stockId());

                if (sellOrderOpt.isPresent()) {
                    stockTradeService.processTradeOrder(buyOrder, sellOrderOpt.get());
                } else {
                    stockOrderService.unOccupyBuyOrder(buyOrder);
                }

            }

        } catch (Exception ex) {
            logger.error("gwDnA7pS :: error occurred in processOrders", ex);
            stop();
            start();
        }
    }

    public void stop() {
        scheduler.shutdown();
    }

    private int getLimit() {
        try {
            return Integer.parseInt(ApplicationPropsLoader.getProperty("order.queue.limit"));
        } catch (Exception ex) {
            logger.error("YwJh3f7K :: error in getting property order.queue.limit");
            return PROCESS_ORDERS_DEFAULT_LIMIT;
        }
    }

}