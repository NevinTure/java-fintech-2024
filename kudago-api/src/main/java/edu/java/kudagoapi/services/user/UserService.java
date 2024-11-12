package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.dtos.user.*;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> register(RegisterRequest request);

    ResponseEntity<Object> changePassword(ChangePasswordRequest request);

    ResponseEntity<TwoFAResponse> enable2FA();

    ResponseEntity<Object> disable2FA(Disable2FARequest request);
}
