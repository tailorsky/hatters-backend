package com.hatterscraft.hatters_backend.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("=== JwtAuthFilter triggered ===");
        System.out.println("AUTH HEADER: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String userId = jwtService.extractUserId(token);

            // достаем роли из токена
            List<String> roles = jwtService.extractRoles(token);

            // превращаем в GrantedAuthority
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
            );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // кладем аутентификацию в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getClass() + " - " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
