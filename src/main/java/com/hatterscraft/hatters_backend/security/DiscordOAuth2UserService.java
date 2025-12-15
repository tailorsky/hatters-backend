package com.hatterscraft.hatters_backend.security;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import com.hatterscraft.hatters_backend.model.Role;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;

@Service
public class DiscordOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RestTemplate rest = new RestTemplate();

    public DiscordOAuth2UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String token = userRequest.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("User-Agent", "HattersBackend/1.0");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> resp = rest.exchange(
            "https://discord.com/api/users/@me",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> userAttributes = resp.getBody();

        String discordId = (String) userAttributes.get("id");
        String username = (String) userAttributes.get("username");
        String discriminator = (String) userAttributes.get("discriminator");
        String email = (String) userAttributes.get("email");
        String avatar = (String) userAttributes.get("avatar");
        String locale = (String) userAttributes.get("locale");

        User user = userRepository.findByDiscordId(discordId)
            .map(u -> {
                u.setUsername(username);
                u.setDiscriminator(discriminator);
                u.setEmail(email);
                u.setLocale(locale);
                u.setFirstLogin(LocalDateTime.now());

                String avatarUrl = (avatar != null && !avatar.isEmpty())
                        ? "https://cdn.discordapp.com/avatars/" + u.getDiscordId() + "/" + avatar + ".png"
                        : "https://cdn.discordapp.com/embed/avatars/0.png";

                u.setAvatar(avatar != null ? avatar : "null");
                u.setAvatarUrl(avatarUrl);

                return userRepository.save(u);
            })
            .orElseGet(() -> {
                User u = new User();
                u.setDiscordId(discordId);
                u.setUsername(username);
                u.setDiscriminator(discriminator);
                u.setEmail(email);
                u.setLocale(locale);
                u.setFirstLogin(LocalDateTime.now());

                String avatarUrl = (avatar != null && !avatar.isEmpty())
                        ? "https://cdn.discordapp.com/avatars/" + discordId + "/" + avatar + ".png"
                        : "https://cdn.discordapp.com/embed/avatars/0.png";

                u.setAvatar(avatar != null ? avatar : "null");
                u.setAvatarUrl(avatarUrl);

                return userRepository.save(u);
            });

        // Получаем роли пользователя
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        // Генерация JWT с ролями
        String jwt = jwtService.generateToken(
                user.getId().toString(),
                user.getUsername(),
                roleNames,           // список ролей
                Map.of()             // дополнительные claims, если нужны
        );

        userAttributes.put("jwt", jwt);
        userAttributes.put("roles", roleNames);

        // Префикс ROLE_ для Spring Security
        String[] authorities = roleNames.stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .toArray(String[]::new);

        return new DefaultOAuth2User(
                AuthorityUtils.createAuthorityList(authorities),
                userAttributes,
                "id"
        );
    }
}
