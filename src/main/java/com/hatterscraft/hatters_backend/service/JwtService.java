package com.hatterscraft.hatters_backend.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.hatterscraft.hatters_backend.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    private final String SECRET = "hatterssecret"; // в реальном проекте хранить безопасно
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 день

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("provider", user.getProvider())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
