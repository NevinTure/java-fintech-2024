package edu.java.kudagoapi.exceptions;

public class LocationNotFoundApiException extends NotFoundApiException {

    public LocationNotFoundApiException(Long id) {
        super(String.format("Location with id %d not found", id));
    }
}
