package kz.nkoldassov.stocktrading.exception;

import io.javalin.http.HttpResponseException;

public class NoUserDataException extends HttpResponseException {
    public NoUserDataException(String message) {
        super(404, message);
    }
}