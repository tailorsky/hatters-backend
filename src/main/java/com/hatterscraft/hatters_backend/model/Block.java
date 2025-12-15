package com.hatterscraft.hatters_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Block {
    private String type; // "heading", "paragraph", "image", "list" и т.д.
    private String text; // для заголовков и параграфов
    private Integer level; // для заголовков (h1, h2...)
    private String url;   // для изображений
    private String alt;   // описание изображения
}