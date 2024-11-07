package edu.java.kudagoapi.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;

public class ExceptionHandlerUtils {

    public static List<String> getViolatedFields(BindingResult bindingResult) {
        return bindingResult
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
