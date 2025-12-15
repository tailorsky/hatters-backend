package com.hatterscraft.hatters_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatterscraft.hatters_backend.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDiscordId(String discordId);
    Optional<User> findByMinecraftNickname(String minecraftNickname);
}
