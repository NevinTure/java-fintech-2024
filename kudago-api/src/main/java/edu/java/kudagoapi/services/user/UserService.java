package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.dtos.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> register(RegisterRequest request);

}
