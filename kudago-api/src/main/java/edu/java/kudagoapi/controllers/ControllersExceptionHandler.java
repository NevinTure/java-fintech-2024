package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.ApiErrorResponse;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;

@RestControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestApiException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
}
