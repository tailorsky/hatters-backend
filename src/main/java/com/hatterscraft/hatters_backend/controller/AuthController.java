package com.hatterscraft.hatters_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hatterscraft.hatters_backend.dto.AuthDTO;
import com.hatterscraft.hatters_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Регистрация обычного пользователя
    @PostMapping("/register")
    public ResponseEntity<AuthDTO.AuthResponse> register(@RequestBody AuthDTO.RegisterRequest request) {
        AuthDTO.AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // Логин обычного пользователя
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.AuthResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        AuthDTO.AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Заглушка OAuth
    @GetMapping("/{provider}")
    public ResponseEntity<String> oauthRedirect(@PathVariable String provider) {
        // Здесь фронт пока получает фиктивного юзера
        String demoJwt = authService.loginDemo(provider);
        return ResponseEntity.ok(demoJwt);
    }
}
