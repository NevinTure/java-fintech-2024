package edu.java.kudagoapi.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private int code;
    private String message;
}
