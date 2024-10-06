package edu.java.kudagoapi.exceptions;

public class CategoryNotFoundApiException extends NotFoundApiException {

    public CategoryNotFoundApiException(long id) {
        super(String.format("Category with id %d not found", id));
    }
}
