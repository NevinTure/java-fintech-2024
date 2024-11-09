package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.*;
import edu.java.kudagoapi.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<TwoFAResponse> enable2FA() {
        return userService.enable2FA();
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<Object> disable2FA() {
        return userService.disable2FA();
    }
}
