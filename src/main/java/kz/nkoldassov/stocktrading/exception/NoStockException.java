package kz.nkoldassov.stocktrading.exception;

import io.javalin.http.HttpResponseException;

public class NoStockException extends HttpResponseException {
    public NoStockException(String message) {
        super(404, message);
    }
}