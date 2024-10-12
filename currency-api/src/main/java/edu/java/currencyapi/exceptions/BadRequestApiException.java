package edu.java.currencyapi.exceptions;

public class BadRequestApiException extends ApiException {

    public static final int CODE = 400;

    public BadRequestApiException(String message) {
        super(message, CODE);
    }
}
