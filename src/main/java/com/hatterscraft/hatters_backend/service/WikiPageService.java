package com.hatterscraft.hatters_backend.service;

import com.hatterscraft.hatters_backend.dto.WikiPageRequestDto;
import com.hatterscraft.hatters_backend.model.WikiPage;
import com.hatterscraft.hatters_backend.model.WikiPageStatus;
import com.hatterscraft.hatters_backend.repository.WikiPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WikiPageService {

    private final WikiPageRepository repo;

    public WikiPageService(WikiPageRepository repo) {
        this.repo = repo;
    }

    public List<WikiPage> getPublishedByCategory(String category) {
        return repo.findByCategoryAndStatus(
                category,
                WikiPageStatus.PUBLISHED
        );
    }

    public Optional<WikiPage> getPublishedBySlug(String category, String slug) {
        return repo.findByCategoryAndSlugAndStatus(
                category,
                slug,
                WikiPageStatus.PUBLISHED
        );
    }

    
    public List<WikiPage> getAllPublished() {
        return repo.findAll()
                .stream()
                .filter(p -> p.getStatus() == WikiPageStatus.PUBLISHED)
                .toList();
    }


    public List<WikiPage> getAllPages() {
        return repo.findAll();
    }

    public Optional<WikiPage> getPageById(Long id) {
        return repo.findById(id);
    }

    public WikiPage createPage(WikiPage page) {
        page.setStatus(WikiPageStatus.DRAFT);
        return repo.save(page);
    }

    public WikiPage updatePage(Long id, WikiPageRequestDto dto) {
        return repo.findById(id)
                .map(page -> {
                    page.setTitle(dto.getTitle());
                    page.setCategory(dto.getCategory());
                    page.setSlug(dto.getSlug());
                    page.setCategoryTitle(dto.getCategoryTitle());
                    page.setContent(dto.getContent());
                    return repo.save(page);
                })
                .orElseThrow(() -> new RuntimeException("Page not found"));
    }



    public WikiPage publish(Long id) {
        return repo.findById(id)
                .map(page -> {
                    page.setStatus(WikiPageStatus.PUBLISHED);
                    return repo.save(page);
                })
                .orElseThrow(() -> new RuntimeException("Page not found"));
    }

    public WikiPage archive(Long id) {
        return repo.findById(id)
                .map(page -> {
                    page.setStatus(WikiPageStatus.ARCHIVED);
                    return repo.save(page);
                })
                .orElseThrow(() -> new RuntimeException("Page not found"));
    }

    public void deletePage(Long id) {
        repo.deleteById(id);
    }
}
