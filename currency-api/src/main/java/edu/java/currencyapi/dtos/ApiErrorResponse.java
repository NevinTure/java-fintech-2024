package edu.java.currencyapi.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private int code;
    private String message;
}
