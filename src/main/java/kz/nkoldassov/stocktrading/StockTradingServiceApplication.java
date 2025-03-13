package kz.nkoldassov.stocktrading;

import kz.nkoldassov.stocktrading.config.LiquibaseRunner;

public class StockTradingServiceApplication {

    public static void main(String[] args) {

        System.out.println("Starting Stock Trading Service...");

        LiquibaseRunner.runMigrations();

        System.out.println("Database initialized!");

    }


    //Microservice Idea: Stock Trading Order Matching System
    //This service will handle buy/sell orders and match them efficiently. It will involve:
    //✅ Multithreading – to process multiple orders concurrently.
    //✅ Transaction Locking – to prevent race conditions in order execution.
    //✅ Concurrency Control – to manage simultaneous trade executions.
    //✅ Queue & Event Handling – to simulate an exchange-like environment.

    //Core Features
    //Place Buy/Sell Orders – Users submit orders with stock symbol, price, and quantity.
    //Order Matching Engine – Matches buy orders with sell orders efficiently.
    //Transaction Handling – Ensures atomic execution of trades.
    //Concurrency Management – Uses locks to prevent duplicate order executions.
    //In-Memory Order Book – Keeps track of pending orders.
    //Logging & Monitoring – Tracks executed trades and order status.

    //Tech Stack (Without Spring)
    //Javalin or Java EE (JAX-RS) for REST APIs
    //HikariCP + JDBC for database transactions
    //ExecutorService for multithreaded order processing
    //ReentrantLock for concurrency control
    //Gradle as the build tool

}
