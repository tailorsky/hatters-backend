package com.hatterscraft.hatters_backend;
import com.hatterscraft.hatters_backend.controller.NewsController;
import com.hatterscraft.hatters_backend.model.News;
import com.hatterscraft.hatters_backend.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsController controller;

    @Test
    void getAllNews_shouldReturnList() {
        News news = new News();
        news.setId(1L);
        news.setTitle("Test");
        news.setCreatedAt(LocalDateTime.now());
        when(newsRepository.findAll()).thenReturn(List.of(news));

        var result = controller.getAllNews();
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getTitle());
    }

    @Test
    void createNews_shouldReturnSavedNews() {
        News news = new News();
        news.setTitle("New News");
        when(newsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        News result = controller.createNews(news);
        assertEquals("New News", result.getTitle());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void updateNews_shouldReturnUpdatedNews() {
        News existing = new News();
        existing.setId(1L);
        existing.setTitle("Old Title");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(newsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        News update = new News();
        update.setTitle("Updated Title");

        News result = controller.updateNews(1L, update);
        assertEquals("Updated Title", result.getTitle());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void deleteNews_shouldCallRepository() {
        doNothing().when(newsRepository).deleteById(1L);
        controller.deleteNews(1L);
        verify(newsRepository, times(1)).deleteById(1L);
    }
}
