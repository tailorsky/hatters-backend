package com.hatterscraft.hatters_backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hatterscraft.hatters_backend.dto.AuthDTO;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.service.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService; // нужно будет создать сервис для JWT

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // В реальном проекте хешировать!
        user.setProvider("local");

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthDTO.AuthResponse(token, user.getUsername(), user.getProvider());
    }

    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user);
        return new AuthDTO.AuthResponse(token, user.getUsername(), user.getProvider());
    }

    // Для демо OAuth-провайдеров
    public String loginDemo(String provider) {
        User demoUser = new User();
        demoUser.setId((long) (Math.random() * 1000));
        demoUser.setUsername(provider.equals("discord") ? "Creeper#1234" : provider.equals("google") ? "Steve Alex" : "TarasPlay");
        demoUser.setProvider(provider);
        demoUser.setPassword("demo");

        return jwtService.generateToken(demoUser);
    }
}
