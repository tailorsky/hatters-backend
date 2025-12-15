package com.hatterscraft.hatters_backend.dto;

import lombok.*;
import java.time.*;

@Getter
@Setter

public class NewsDTO {
    private Long id;
    private String title;
    private String imageUrl;

    private String content;
    LocalDateTime createdAt;

    public NewsDTO(Long id, String title, String imageUrl, String content, LocalDateTime createdAt){
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.createdAt = createdAt;
    }
}
