package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import kz.nkoldassov.stocktrading.controller.StockTradeController;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockTradeToSellDto;
import kz.nkoldassov.stocktrading.repository.*;
import kz.nkoldassov.stocktrading.repository.impl.*;
import kz.nkoldassov.stocktrading.scheduler.StockTradesScheduler;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import kz.nkoldassov.stocktrading.service.impl.StockTradeServiceImpl;
import kz.nkoldassov.stocktrading.validator.AnnotationValidator;
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

        StockBuyTradeQueueRepository stockBuyTradeQueueRepository = new StockBuyTradeQueueRepositoryImpl();
        StockSellTradeQueueRepository stockSellTradeQueueRepository = new StockSellTradeQueueRepositoryImpl();
        UserStockRepository userStockRepository = new UserStockRepositoryImpl();
        UserDataRepository userDataRepository = new UserDataRepositoryImpl();
        StockRepository stockRepository = new StockRepositoryImpl();

        logger.info("MVoHZY9V :: Repositories initialized");

        StockTradeService stockTradeService = new StockTradeServiceImpl(
                stockBuyTradeQueueRepository,
                stockSellTradeQueueRepository,
                userStockRepository,
                userDataRepository,
                stockRepository
        );

        logger.info("L2mBz5mq :: Services initialized");

        StockTradesScheduler stockTradesScheduler = new StockTradesScheduler(stockTradeService);
        stockTradesScheduler.start();

        logger.info("8BJhgFTB :: Schedulers initialized and started");

        stockOperationAPIs(app, stockTradeService);

    }

    private static void stockOperationAPIs(Javalin app, StockTradeService stockTradeService) {

        StockTradeController controller = new StockTradeController(stockTradeService);

        app.post("/buy", ctx -> {
            StockTradeToBuyDto tradeToBuyDto = ctx.bodyAsClass(StockTradeToBuyDto.class);
            AnnotationValidator.validate(tradeToBuyDto);
            controller.placeBuyOrder().handle(ctx);
        });

        app.post("/sell", ctx -> {
            StockTradeToSellDto tradeToSellDto = ctx.bodyAsClass(StockTradeToSellDto.class);
            AnnotationValidator.validate(tradeToSellDto);
            controller.placeSellOrder().handle(ctx);
        });

    }

}