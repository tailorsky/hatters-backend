package com.hatterscraft.hatters_backend.controller;

import com.hatterscraft.hatters_backend.model.News;
import com.hatterscraft.hatters_backend.dto.NewsDTO;
import com.hatterscraft.hatters_backend.repository.NewsRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import java.util.List;

@RestController
public class PublicNewsController {

    private final NewsRepository newsRepository;

    public PublicNewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping("/public/news")
    public List<NewsDTO> getAllNews() {
        return newsRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(news -> new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getImageUrl(),
                news.getContent(),
                news.getCreatedAt()
            ))
            .toList();
    }

}
