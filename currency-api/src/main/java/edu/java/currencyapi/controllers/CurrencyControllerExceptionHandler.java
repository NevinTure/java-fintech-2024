package edu.java.currencyapi.controllers;

import edu.java.currencyapi.dtos.ApiErrorResponse;
import edu.java.currencyapi.exceptions.*;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.Duration;
import java.util.List;

@RestControllerAdvice
public class CurrencyControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestApiException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableApiException.class)
    public ResponseEntity<Object> handleServiceUnavailable(ServiceUnavailableApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", String.valueOf(Duration.ofHours(1).toSeconds()))
                .body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> violatedField = ex
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(v -> {
                    FieldError fieldError = (FieldError) v;
                    return String.format(
                            "%s %s", fieldError.getField(), fieldError.getDefaultMessage());
                })
                .sorted()
                .toList();
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                String.format("Invalid request params: %s", String.join(", ", violatedField))
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundApiException.class)
    public ResponseEntity<Object> handleCategoryNotFound(NotFoundApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
