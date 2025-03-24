package kz.nkoldassov.stocktrading.scheduler;

import kz.nkoldassov.stocktrading.service.StockTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockTradesScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockTradesScheduler.class);

    private final StockTradeService stockTradeService;

    public StockTradesScheduler(StockTradeService stockTradeService) {
        this.stockTradeService = stockTradeService;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        scheduler.scheduleAtFixedRate(this::processOrders, 0, 2, TimeUnit.MINUTES);
    }

    private void processOrders() {
        logger.info("MxsCk8fT :: Processing operations at: {}", LocalDateTime.now());

        try {
            stockTradeService.processTrades();
        } catch (Exception ex) {
            logger.error("gwDnA7pS :: error occurred in processTrades", ex);
            stop();
            start();
        }
    }

    public void stop() {
        scheduler.shutdown();
    }

}