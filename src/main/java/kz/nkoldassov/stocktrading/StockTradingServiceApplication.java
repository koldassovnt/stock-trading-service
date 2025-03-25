package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import kz.nkoldassov.stocktrading.controller.StockController;
import kz.nkoldassov.stocktrading.controller.StockOrderController;
import kz.nkoldassov.stocktrading.controller.UserPortfolioController;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToSellDto;
import kz.nkoldassov.stocktrading.repository.*;
import kz.nkoldassov.stocktrading.repository.impl.*;
import kz.nkoldassov.stocktrading.scheduler.StockTradeScheduler;
import kz.nkoldassov.stocktrading.service.*;
import kz.nkoldassov.stocktrading.service.impl.*;
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

        StockBuyOrderQueueRepository stockBuyOrderQueueRepository = new StockBuyOrderQueueRepositoryImpl();
        StockSellOrderQueueRepository stockSellOrderQueueRepository = new StockSellOrderQueueRepositoryImpl();
        StockTradeOperationRepository stockTradeOperationRepository = new StockTradeOperationRepositoryImpl();
        UserDataRepository userDataRepository = new UserDataRepositoryImpl();
        StockRepository stockRepository = new StockRepositoryImpl();

        logger.info("MVoHZY9V :: Repositories initialized");

        StockOrderService stockOrderService = new StockOrderServiceImpl(
                stockBuyOrderQueueRepository,
                stockSellOrderQueueRepository
        );

        StockService stockService = new StockServiceImpl(stockRepository);
        UserDataService userDataService = new UserDataServiceImpl(userDataRepository);
        StockTradeService stockTradeService = new StockTradeServiceImpl(stockTradeOperationRepository);
        UserStockService userStockService = new UserStockServiceImpl();

        logger.info("L2mBz5mq :: Services initialized");

        StockOrderController stockOrderController = new StockOrderController(
                stockOrderService,
                userDataService,
                stockService);

        UserPortfolioController userPortfolioController = new UserPortfolioController(userStockService);
        StockController stockController = new StockController(stockService);

        logger.info("7wnMDf2A :: Controllers initialized");

        StockTradeScheduler stockTradeScheduler = new StockTradeScheduler(
                stockTradeService,
                stockOrderService);

        stockTradeScheduler.start();

        logger.info("8BJhgFTB :: Schedulers initialized and started");

        initStockOrderAPIs(app, stockOrderController);
        initUserPortfolioAPIs(app, userPortfolioController);
        initStockAPIs(app, stockController);

        logger.info("d9TwAmJV :: APIs initialized");

    }

    private static void initStockOrderAPIs(Javalin app, StockOrderController controller) {

        app.post("/orders/v1/buy", ctx -> {
            StockOrderToBuyDto orderToBuyDto = ctx.bodyAsClass(StockOrderToBuyDto.class);
            AnnotationValidator.validate(orderToBuyDto);
            controller.placeBuyOrder().handle(ctx);
        });

        app.post("/orders/v1/sell", ctx -> {
            StockOrderToSellDto orderToSellDto = ctx.bodyAsClass(StockOrderToSellDto.class);
            AnnotationValidator.validate(orderToSellDto);
            controller.placeSellOrder().handle(ctx);
        });

    }

    private static void initUserPortfolioAPIs(Javalin app, UserPortfolioController controller) {
        app.get("/user-portfolios/v1/{userId}", ctx -> controller.loadUserPortfolio().handle(ctx));
    }

    private static void initStockAPIs(Javalin app, StockController controller) {
        app.get("/stocks/v1", ctx -> controller.loadStocks().handle(ctx));
    }

}