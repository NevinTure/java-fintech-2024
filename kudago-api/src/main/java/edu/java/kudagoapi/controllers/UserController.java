package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.user.*;
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

    @PatchMapping("/enable-2fa")
    public ResponseEntity<TwoFAResponse> enable2FA() {
        return userService.enable2FA();
    }

    @PatchMapping("/disable-2fa")
    public ResponseEntity<Object> disable2FA(@Valid @RequestBody Disable2FARequest request) {
        return userService.disable2FA(request);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }
}
