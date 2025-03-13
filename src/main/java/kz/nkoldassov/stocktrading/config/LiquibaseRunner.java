package kz.nkoldassov.stocktrading.config;

import liquibase.command.CommandScope;
import liquibase.exception.CommandExecutionException;

public class LiquibaseRunner {

    public static void runMigrations() {
        try {
            new CommandScope("update")
                    .addArgumentValue("defaultsFile", "liquibase.properties")
                    .execute();
        } catch (CommandExecutionException e) {
            throw new RuntimeException("Error running Liquibase migrations", e);
        }

    }

}