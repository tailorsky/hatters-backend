package com.hatterscraft.hatters_backend.config;

import com.hatterscraft.hatters_backend.security.DiscordOAuth2UserService;
import com.hatterscraft.hatters_backend.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DiscordOAuth2UserService discordOAuth2UserService;

    public SecurityConfig(DiscordOAuth2UserService discordOAuth2UserService) {
        this.discordOAuth2UserService = discordOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {

        AuthenticationSuccessHandler successHandler = (request, response, authentication) -> {
            String jwt = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) authentication.getPrincipal())
                    .getAttribute("jwt");
            response.sendRedirect("http://localhost:5173/?token=" + jwt);
        };

        http
            .cors()
            .and()
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/user/updateStats",
                        "/api/user/check-minecraft-password/**",
                        "/api/user/by-nick/**",
                        "/static/skins/**"
                    ).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/", "/login**", "/error", "/public/**", "/oauth2/**", "/api/wiki/published", "/api/wiki/*/*").permitAll()

                .requestMatchers("/admin/**").hasRole("ADMIN")

                .requestMatchers("/moderation/**").hasAnyRole("MODERATOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(discordOAuth2UserService))
                .successHandler(successHandler)
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
