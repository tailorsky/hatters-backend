package com.hatterscraft.hatters_backend.controller;

import com.hatterscraft.hatters_backend.dto.WikiPageRequestDto;
import com.hatterscraft.hatters_backend.model.WikiPage;
import com.hatterscraft.hatters_backend.service.WikiPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wiki")
public class WikiPageController {

    private final WikiPageService service;

    public WikiPageController(WikiPageService service) {
        this.service = service;
    }

    // =====================
    // ПУБЛИЧНЫЕ ENDPOINTS
    // =====================

    @GetMapping("/published")
    public List<WikiPage> getAllPublished() {
        return service.getAllPublished();
    }

    @GetMapping("/category/{category}")
    public List<WikiPage> getPublishedByCategory(
            @PathVariable String category
    ) {
        return service.getPublishedByCategory(category);
    }

    // Конкретная статья (ТОЛЬКО PUBLISHED)
    @GetMapping("/{category}/{slug}")
    public ResponseEntity<WikiPage> getPublishedBySlug(
            @PathVariable String category,
            @PathVariable String slug
    ) {
        return service.getPublishedBySlug(category, slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================
    // АДМИНКА
    // =====================

    @GetMapping
    public List<WikiPage> getAllPages() {
        return service.getAllPages();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<WikiPage> getPageById(@PathVariable Long id) {
        return service.getPageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WikiPage> createPage(
            @RequestBody WikiPageRequestDto dto
    ) {
        WikiPage page = new WikiPage();
        page.setCategory(dto.getCategory());
        page.setTitle(dto.getTitle());
        page.setSlug(dto.getSlug());
        page.setCategoryTitle(dto.getCategoryTitle());
        page.setContent(dto.getContent());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createPage(page));
    }


    @PutMapping("/{id}")
    public ResponseEntity<WikiPage> updatePage(
            @PathVariable Long id,
            @RequestBody WikiPageRequestDto dto
    ) {
        try {
            return ResponseEntity.ok(service.updatePage(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }





    @PutMapping("/{id}/publish")
    public WikiPage publish(@PathVariable Long id) {
        return service.publish(id);
    }

    @PutMapping("/{id}/archive")
    public WikiPage archive(@PathVariable Long id) {
        return service.archive(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        service.deletePage(id);
        return ResponseEntity.noContent().build();
    }
}
