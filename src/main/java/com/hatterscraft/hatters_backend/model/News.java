package com.hatterscraft.hatters_backend.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String preview;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
