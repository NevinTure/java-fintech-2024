package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.ApiErrorResponse;
import edu.java.kudagoapi.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handleHibernateConstraintViolation(PSQLException ex) {
        String message = ex.getServerErrorMessage() == null ? ex.getMessage() : ex.getServerErrorMessage().getDetail();
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
                message
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SnapshotNotFoundApiException.class)
    public ResponseEntity<Object> handleSnapshotNotFound(SnapshotNotFoundApiException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> violatedField = getViolatedFields(ex);
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                String.format("Invalid request params: %s", String.join(", ", violatedField))
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private List<String> getViolatedFields(MethodArgumentNotValidException ex) {
        return ex
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
    }
}
