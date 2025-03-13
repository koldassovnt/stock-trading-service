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
            logger.info("changelogFile = {}", changelogFile);
            logger.info("database.url = {}", url);
            logger.info("database.username = {}", username);

            new CommandScope("update")
                    .addArgumentValue("changelogFile", changelogFile)
                    .addArgumentValue("url", url)
                    .addArgumentValue("username", username)
                    .addArgumentValue("password", password)
                    .execute();
        } catch (CommandExecutionException e) {
            throw new RuntimeException("Error running Liquibase migrations", e);
        }
    }


}