package com.hatterscraft.hatters_backend.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hatterscraft.hatters_backend.model.Role;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.security.JwtService;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal String userId) {
        Long id = Long.parseLong(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("balance", user.getBalance());
        map.put("avatar", user.getAvatar());
        map.put("locale", user.getLocale());
        map.put("discordId", user.getDiscordId());
        map.put("firstLogin", user.getFirstLogin());
        map.put("avatarUrl", user.getAvatarUrl());
        map.put("roles", roleNames);
        map.put("minecraftNickname", user.getMinecraftNickname());
        map.put("minecraftUuid", user.getUuid());
        map.put("played", user.getPlayTimeSeconds());
        map.put("level", user.getLevel());
        return map;
    }



    @GetMapping("/all-users")
    public List<Map<String, Object>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<Map<String, Object>> result = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("balance", u.getBalance());
            map.put("avatarUrl", u.getAvatarUrl() != null ? u.getAvatarUrl() : "https://cdn.discordapp.com/embed/avatars/0.png");
            map.put("locale", u.getLocale());
            map.put("discordId", u.getDiscordId());
            map.put("firstLogin", u.getFirstLogin());
            map.put("roles", u.getRoles().stream().map(Role::getName).toList());
            map.put("minecraftNickname", u.getMinecraftNickname());
            map.put("minecraftUuid", u.getUuid());
            result.add(map);
        }

        return result;
    }
}
