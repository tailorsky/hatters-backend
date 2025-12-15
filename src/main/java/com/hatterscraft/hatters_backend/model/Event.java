package com.hatterscraft.hatters_backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String videoUrl;

    private String imageUrl;

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    @ElementCollection
    @CollectionTable(name = "event_rewards", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "reward")
    private List<String> rewards;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventCondition> conditions;
}
