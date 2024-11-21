package edu.java.kudagoapi.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty(required = true)
    @Size(min = 2)
    private String name;
    @JsonProperty(required = true)
    @Email
    private String email;
    @JsonProperty(required = true)
    @Size(min = 8)
    private String password;
}
