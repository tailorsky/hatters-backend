package com.hatterscraft.hatters_backend.repository;

import com.hatterscraft.hatters_backend.model.UserEventReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRewardRepository extends JpaRepository<UserEventReward, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}
