package com.hatterscraft.hatters_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WikiPageRequestDto {
    private String category;
    private String title;
    private String slug;
    private String categoryTitle;
    private JsonNode content;
}