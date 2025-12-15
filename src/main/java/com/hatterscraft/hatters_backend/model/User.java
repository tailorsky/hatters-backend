package com.hatterscraft.hatters_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="discord_id", unique=true, nullable=true)
    private String discordId;

    @Column(name="minecraft_nickname", unique=true, nullable=true)
    private String minecraftNickname;
    @Column(name="uuid_minecraft", unique=true, nullable=true)
    private String uuid;
    @Column(name="minecraft_password_hash", nullable=true)
    private String minecraftPasswordHash;
    @Column(name="skin_url", unique=true, nullable=true)
    private String skinUrl;

    @Column(nullable = false)
    private Integer balance = 0;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    private String username;
    private String discriminator;
    private String email;
    private String avatar;
    private String locale;

    @Column(name = "first_login", nullable = false, updatable = false)
    private LocalDateTime firstLogin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Item> inventory = new ArrayList<>();

    // 🔹 Добавляем ManyToMany для ролей
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name="play_time_seconds", nullable=false)
    private Long playTimeSeconds = 0L; // количество секунд, которые игрок провёл на сервере

    @Column(name="quests_completed", nullable=false)
    private Integer questsCompleted = 0;

    @Column(name="level", nullable=false)
    private Integer level = 0;
}
