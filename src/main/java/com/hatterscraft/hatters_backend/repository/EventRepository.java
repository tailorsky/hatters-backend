package com.hatterscraft.hatters_backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hatterscraft.hatters_backend.model.Event;

public interface EventRepository extends JpaRepository <Event, Long>{
    public List<Event> findByEventEndBefore(LocalDateTime time);
    public List<Event> findByEventStartAfter(LocalDateTime time);

    public List<Event> findTop3ByOrderByEventStartDesc();

    @Query("SELECT e FROM Event e WHERE e.eventStart <= :now AND e.eventEnd >= :now")
    public List<Event> findCurrentEvent(@Param("now") LocalDateTime now);
}
