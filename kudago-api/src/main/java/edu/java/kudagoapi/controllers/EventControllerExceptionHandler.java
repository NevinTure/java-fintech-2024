package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.ApiErrorResponse;
import edu.java.kudagoapi.exceptions.EventNotFoundApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class EventControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EventNotFoundApiException.class)
    public ResponseEntity<Object> handleCategoryNotFound(EventNotFoundApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
