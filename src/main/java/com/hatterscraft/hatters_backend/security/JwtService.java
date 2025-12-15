package com.hatterscraft.hatters_backend.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long expirationMs = 3600000;

    public String generateToken(String userId, String username, List<String> roles, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userId)
                .claim("username", username)
                .claim("roles", roles) // кладём роли в токен
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String extractUserId(String token) {
        return validateToken(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) validateToken(token).getBody().get("roles", List.class);
    }
}
