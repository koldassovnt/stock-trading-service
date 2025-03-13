package kz.nkoldassov.stocktrading;

import io.javalin.Javalin;
import kz.nkoldassov.stocktrading.config.ApplicationPropsLoader;
import kz.nkoldassov.stocktrading.config.LiquibaseRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

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

        app.get("/tables", ctx -> {
            try (Connection conn = DriverManager.getConnection(
                    ApplicationPropsLoader.getProperty("database.url"),
                    ApplicationPropsLoader.getProperty("database.username"),
                    ApplicationPropsLoader.getProperty("database.password")
            )) {
                ResultSet rs = conn.createStatement().executeQuery("SHOW TABLES");
                StringBuilder tables = new StringBuilder();
                while (rs.next()) {
                    tables.append(rs.getString(1)).append("\n");
                }
                ctx.result("Existing Tables:\n" + tables);
            }
        });

    }

}
