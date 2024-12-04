package edu.java.kudagoapi.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disable2FARequest {
    @NotNull
    private String code;
}
