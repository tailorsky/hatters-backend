package com.hatterscraft.hatters_backend.model;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user_event_rewards")
@Getter
@Setter

public class UserEventReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long eventId;

    private LocalDateTime rewardedAt;
}
