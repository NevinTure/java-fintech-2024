package edu.java.kudagoapi.exceptions;

public class NotFoundApiException extends ApiException {

    private static final int CODE = 404;

    public NotFoundApiException(String message) {
        super(message, CODE);
    }
}
