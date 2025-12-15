package com.hatterscraft.hatters_backend.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // ==========================================================
    //  LINKING MINECRAFT ACCOUNT (OFFLINE-MODE)
    // ==========================================================
    @PostMapping("/{id}/link-minecraft")
    public ResponseEntity<?> linkMinecraft(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nickname = payload.get("nickname");
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Minecraft nickname is required"));
        }

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Генерируем offline UUID
        UUID offlineUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + nickname).getBytes());

        user.setMinecraftNickname(nickname);
        user.setUuid(offlineUuid.toString());
        userRepository.save(user);

        // Добавляем в whitelist Minecraft-сервера
        try {
            String whitelistUrl = "http://localhost:8081/whitelist/add";
            Map<String, String> whitelistPayload = Map.of("nickname", nickname);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer super_secret_token");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(whitelistPayload, headers);
            restTemplate.postForEntity(whitelistUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("Failed to add to whitelist: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "status", "linked",
                "minecraftNick", nickname,
                "uuid", offlineUuid.toString()
        ));
    }

    @DeleteMapping("/{id}/unlink-minecraft")
    public ResponseEntity<?> unlinkMinecraft(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getMinecraftNickname() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Minecraft account is not linked"));
        }

        String oldNick = user.getMinecraftNickname();
        user.setMinecraftNickname(null);
        user.setUuid(null);
        userRepository.save(user);

        try {
            String whitelistUrl = "http://localhost:8081/whitelist/remove";
            Map<String, String> whitelistPayload = Map.of("nickname", oldNick);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer super_secret_token");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(whitelistPayload, headers);
            restTemplate.postForEntity(whitelistUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("Failed to remove from whitelist: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "status", "unlinked",
                "previousNick", oldNick
        ));
    }

    // ==========================================================
    //  PASSWORD FOR OFFLINE AUTH
    // ==========================================================
    @PostMapping("/{id}/set-minecraft-password")
    public ResponseEntity<?> setMinecraftPassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String password = payload.get("password");
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setMinecraftPasswordHash(encoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("status", "password_set"));
    }

    @PostMapping("/check-minecraft-password/{nickname}")
    public ResponseEntity<?> checkMinecraftPassword(
            @PathVariable String nickname,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> payload) {

        String expectedToken = "super_secret_token";
        if (authHeader == null || !authHeader.equals("Bearer " + expectedToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        String password = payload.get("password");
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }

        User user = userRepository.findByMinecraftNickname(nickname)
                .orElseThrow(() -> new RuntimeException("User not found: " + nickname));

        if (user.getMinecraftPasswordHash() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Minecraft password not set"));
        }

        boolean matches = new BCryptPasswordEncoder().matches(password, user.getMinecraftPasswordHash());
        return matches
                ? ResponseEntity.ok(Map.of("status", "valid"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
    }

    // ==========================================================
    //  Stats Update from Plugin
    // ==========================================================
    @PostMapping("/updateStats")
    public ResponseEntity<?> updateStats(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> payload) {

        String expectedToken = "super_secret_token";
        if (authHeader == null || !authHeader.equals("Bearer " + expectedToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        String nickname = (String) payload.get("nickname");
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nickname is required"));
        }

        Long playTime = ((Number) payload.getOrDefault("playTimeSeconds", 0)).longValue();
        Integer level = ((Number) payload.getOrDefault("level", 0)).intValue();
        Integer questsCompleted = ((Number) payload.getOrDefault("questsCompleted", 0)).intValue();

        User user = userRepository.findByMinecraftNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Игрок не найден: " + nickname));

        user.setPlayTimeSeconds(playTime);
        user.setLevel(level);
        user.setQuestsCompleted(questsCompleted);

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("status", "success"));
    }

    // ==========================================================
    //  User Info
    // ==========================================================
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================================
    //  SKIN MANAGEMENT (БЕЗ ИЗМЕНЕНИЙ)
    // ==========================================================
    @PostMapping("/{id}/upload-skin")
    public ResponseEntity<?> uploadSkin(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            if (!"image/png".equalsIgnoreCase(file.getContentType())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PNG files are allowed"));
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Path uploadDir = Paths.get("uploads/skins");
            Files.createDirectories(uploadDir);
            String filename = "user_" + id + ".png";
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            String skinUrl = "http://localhost:8080/static/skins/" + filename;
            user.setSkinUrl(skinUrl);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("status", "skin_uploaded", "skinUrl", skinUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/by-nick/{nickname}/get-skin")
    public ResponseEntity<?> getSkinByNickname(@PathVariable String nickname) {
        System.out.println(userRepository.findByMinecraftNickname(nickname)
                .map(user -> ResponseEntity.ok(Map.of("skinUrl", user.getSkinUrl())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"))));
        return userRepository.findByMinecraftNickname(nickname)
                .map(user -> ResponseEntity.ok(Map.of("skinUrl", user.getSkinUrl())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }
}