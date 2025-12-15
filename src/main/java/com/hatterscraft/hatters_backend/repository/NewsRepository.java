package com.hatterscraft.hatters_backend.repository;

import com.hatterscraft.hatters_backend.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByCreatedAtDesc();
}
