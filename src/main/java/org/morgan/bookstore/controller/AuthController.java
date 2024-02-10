package org.morgan.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.LoginRequest;
import org.morgan.bookstore.response.LoginResponse;
import org.morgan.bookstore.request.RegisterRequest;
import org.morgan.bookstore.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam(name = "token") String token) {
        System.out.println(token);
        return authService.verifyAccount(token);
    }
}
