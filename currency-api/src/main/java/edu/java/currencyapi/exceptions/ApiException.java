package edu.java.currencyapi.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException {

    private final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
}
