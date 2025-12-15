package com.hatterscraft.hatters_backend.repository;

import com.hatterscraft.hatters_backend.model.WikiPage;
import com.hatterscraft.hatters_backend.model.WikiPageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {

    // Публичные (только опубликованные)
    List<WikiPage> findByCategoryAndStatus(
            String category,
            WikiPageStatus status
    );

    Optional<WikiPage> findByCategoryAndSlugAndStatus(
            String category,
            String slug,
            WikiPageStatus status
    );

    // Админка (все статусы)
    List<WikiPage> findByCategory(String category);
}
