package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.LoginRequest;
import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> loginSuccessful() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
