package edu.java.kudagoapi.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @JsonProperty(required = true)
    private String code;
    @JsonProperty(value = "old_pass", required = true)
    private String oldPassword;
    @JsonProperty(value = "new_pass", required = true)
    private String newPassword;
    @JsonProperty(value = "confirm_pass", required = true)
    private String confirmPassword;
}
