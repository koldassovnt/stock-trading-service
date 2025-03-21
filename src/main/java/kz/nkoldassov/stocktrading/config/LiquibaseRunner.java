package kz.nkoldassov.stocktrading.config;

import liquibase.command.CommandScope;
import liquibase.exception.CommandExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiquibaseRunner {

    private static final Logger logger = LoggerFactory.getLogger(LiquibaseRunner.class);

    public static void runMigrations() {
        try {
            String changelogFile = ApplicationPropsLoader.getProperty("changelogFile");
            String url = ApplicationPropsLoader.getProperty("database.url");
            String username = ApplicationPropsLoader.getProperty("database.username");
            String password = ApplicationPropsLoader.getProperty("database.password");

            logger.info("Liquibase Config:");
            logger.info("qpM5D3nl :: changelogFile = {}", changelogFile);
            logger.info("5mB4rDKx :: database.url = {}", url);
            logger.info("H4PZNFmG :: database.username = {}", username);

            new CommandScope("update")
                    .addArgumentValue("changelogFile", changelogFile)
                    .addArgumentValue("url", url)
                    .addArgumentValue("username", username)
                    .addArgumentValue("password", password)
                    .execute();
        } catch (CommandExecutionException e) {
            throw new RuntimeException("GQ9K53NO :: Error running Liquibase migrations", e);
        }
    }


}