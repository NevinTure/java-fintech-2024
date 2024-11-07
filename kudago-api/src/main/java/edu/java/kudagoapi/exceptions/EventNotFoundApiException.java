package edu.java.kudagoapi.exceptions;

public class EventNotFoundApiException extends NotFoundApiException {

    public EventNotFoundApiException(Long id) {
        super(String.format("Event with id %s not found", id));
    }
}
