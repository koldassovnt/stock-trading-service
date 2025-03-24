package kz.nkoldassov.stocktrading.exception;

import io.javalin.http.HttpResponseException;

public class InvalidBalanceAmountException extends HttpResponseException {
    public InvalidBalanceAmountException(String message) {
        super(400, message);
    }
}