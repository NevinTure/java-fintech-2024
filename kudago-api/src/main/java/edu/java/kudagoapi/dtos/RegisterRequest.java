package edu.java.kudagoapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty(required = true)
    @Min(2)
    private String name;
    @JsonProperty(required = true)
    @Email
    private String email;
    @JsonProperty(required = true)
    @Min(8)
    private String password;
}
