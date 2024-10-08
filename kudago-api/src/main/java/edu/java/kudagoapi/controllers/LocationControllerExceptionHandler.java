package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.ApiErrorResponse;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class LocationControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LocationNotFoundApiException.class)
    public ResponseEntity<Object> handleCategoryNotFound(LocationNotFoundApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
