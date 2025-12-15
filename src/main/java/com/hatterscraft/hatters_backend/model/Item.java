package com.hatterscraft.hatters_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String icon;
    private String rarity; // common, rare, epic
    private Boolean equipped = false;
    private String description;
    private String eventName;
}
