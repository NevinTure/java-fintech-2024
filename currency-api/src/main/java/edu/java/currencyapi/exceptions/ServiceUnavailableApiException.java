package edu.java.currencyapi.exceptions;

public class ServiceUnavailableApiException extends ApiException {

    private static final int CODE = 503;

    public ServiceUnavailableApiException(String message) {
        super(message, CODE);
    }
}
