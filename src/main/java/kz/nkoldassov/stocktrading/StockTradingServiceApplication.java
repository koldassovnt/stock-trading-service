package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import kz.nkoldassov.stocktrading.controller.StockTradeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockTradingServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(StockTradingServiceApplication.class);

    private static int loadServerPort() {
        return Integer.parseInt(ApplicationPropsLoader.getProperty("server.port"));
    }

    public static void main(String[] args) {

        logger.info("Starting Stock Trading Service...");

        int port = loadServerPort();

        Javalin app = Javalin.create().start(port);

        logger.info("Javalin started on port {}", port);

        LiquibaseRunner.runMigrations();

        logger.info("Database initialized!");

        app.get("/", ctx -> ctx.result("Stock Trading Service is running!"));

        stockOperationAPIs(app);

    }

    private static void stockOperationAPIs(Javalin app) {
        app.post("/buy", StockTradeController.placeBuyOrder);
        app.post("/sell", StockTradeController.placeSellOrder);
    }

}