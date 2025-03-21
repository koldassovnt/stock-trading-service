package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import kz.nkoldassov.stocktrading.controller.StockTradeController;
import kz.nkoldassov.stocktrading.dao.StockBuyTradeQueueDao;
import kz.nkoldassov.stocktrading.dao.impl.StockBuyTradeQueueDaoImpl;
import kz.nkoldassov.stocktrading.scheduler.StockTradesScheduler;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import kz.nkoldassov.stocktrading.service.StockTradeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockTradingServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(StockTradingServiceApplication.class);

    private static int loadServerPort() {
        return Integer.parseInt(ApplicationPropsLoader.getProperty("server.port"));
    }

    public static void main(String[] args) {

        logger.info("x3ZLd5U8 :: Starting Stock Trading Service...");

        int port = loadServerPort();

        Javalin app = Javalin.create().start(port);

        logger.info("wiABovUC :: Javalin started on port {}", port);

        LiquibaseRunner.runMigrations();

        logger.info("UZpNvWgn :: Database initialized");

        app.get("/", ctx -> ctx.result("3C4ojF6C :: Stock Trading Service is running!"));

        StockBuyTradeQueueDao stockBuyTradeQueueDao = new StockBuyTradeQueueDaoImpl();
        StockTradeService stockTradeService = new StockTradeServiceImpl(stockBuyTradeQueueDao);

        logger.info("L2mBz5mq :: Services initialized");

        StockTradesScheduler stockTradesScheduler = new StockTradesScheduler(stockTradeService);
        stockTradesScheduler.start();

        logger.info("8BJhgFTB :: Schedulers initialized and started");

        stockOperationAPIs(app, stockTradeService);

    }

    private static void stockOperationAPIs(Javalin app, StockTradeService stockTradeService) {

        StockTradeController controller = new StockTradeController(stockTradeService);

        app.post("/buy", controller.placeBuyOrder());
        app.post("/sell", controller.placeSellOrder());

    }

}