package com.hatterscraft.hatters_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatterscraft.hatters_backend.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
