package edu.java.kudagoapi.exceptions;

public class BadRequestApiException extends ApiException {

    private static final int CODE = 400;

    public BadRequestApiException(String message) {
        super(message, CODE);
    }
}
