package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.dtos.TwoFAResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> register(RegisterRequest request);
    ResponseEntity<TwoFAResponse> enable2FA();
    ResponseEntity<Object> disable2FA();
}
