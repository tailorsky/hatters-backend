package com.hatterscraft.hatters_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String imageUrl;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private List<String> rewards;
}
