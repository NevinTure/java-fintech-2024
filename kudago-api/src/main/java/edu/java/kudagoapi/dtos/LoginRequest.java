package edu.java.kudagoapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    @JsonProperty("remember_me")
    private boolean rememberMe;
}
