package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid RegisterRequest request) {
        return userService.register(request);
    }
}
