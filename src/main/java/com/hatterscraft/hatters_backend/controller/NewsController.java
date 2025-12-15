package com.hatterscraft.hatters_backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hatterscraft.hatters_backend.model.News;
import com.hatterscraft.hatters_backend.repository.NewsRepository;

@RestController
@RequestMapping("/moderation/news")
public class NewsController {

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public News createNews(@RequestBody News news) {
        news.setCreatedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public void deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public News updateNews(@PathVariable Long id, @RequestBody News newsData) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        news.setTitle(newsData.getTitle());
        news.setContent(newsData.getContent());
        news.setImageUrl(newsData.getImageUrl());
        news.setUpdatedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }
}
