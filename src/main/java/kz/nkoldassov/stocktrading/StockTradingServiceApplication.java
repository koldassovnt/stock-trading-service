package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import kz.nkoldassov.stocktrading.controller.StockOrderController;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToSellDto;
import kz.nkoldassov.stocktrading.repository.*;
import kz.nkoldassov.stocktrading.repository.impl.*;
import kz.nkoldassov.stocktrading.scheduler.StockTradeScheduler;
import kz.nkoldassov.stocktrading.service.StockOrderService;
import kz.nkoldassov.stocktrading.service.StockService;
import kz.nkoldassov.stocktrading.service.StockTradeService;
import kz.nkoldassov.stocktrading.service.UserDataService;
import kz.nkoldassov.stocktrading.service.impl.StockOrderServiceImpl;
import kz.nkoldassov.stocktrading.service.impl.StockServiceImpl;
import kz.nkoldassov.stocktrading.service.impl.StockTradeServiceImpl;
import kz.nkoldassov.stocktrading.service.impl.UserDataServiceImpl;
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

        logger.info("L2mBz5mq :: Services initialized");

        StockOrderController stockOrderController = new StockOrderController(
                stockOrderService,
                userDataService,
                stockService);

        logger.info("7wnMDf2A :: Controllers initialized");

        StockTradeScheduler stockTradeScheduler = new StockTradeScheduler(
                stockTradeService,
                stockOrderService);

        stockTradeScheduler.start();

        logger.info("8BJhgFTB :: Schedulers initialized and started");

        stockOperationAPIs(app, stockOrderController);

    }

    private static void stockOperationAPIs(Javalin app, StockOrderController controller) {

        app.post("/buy", ctx -> {
            StockOrderToBuyDto orderToBuyDto = ctx.bodyAsClass(StockOrderToBuyDto.class);
            AnnotationValidator.validate(orderToBuyDto);
            controller.placeBuyOrder().handle(ctx);
        });

        app.post("/sell", ctx -> {
            StockOrderToSellDto orderToSellDto = ctx.bodyAsClass(StockOrderToSellDto.class);
            AnnotationValidator.validate(orderToSellDto);
            controller.placeSellOrder().handle(ctx);
        });

    }

}