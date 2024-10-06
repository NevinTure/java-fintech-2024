package edu.java.kudagoapi.exceptions;

public class LocationNotFoundApiException extends NotFoundApiException {

    public LocationNotFoundApiException(String id) {
        super(String.format("Location with id %s not found", id));
    }
}
