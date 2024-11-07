package edu.java.kudagoapi.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    private boolean rememberMe;
}
