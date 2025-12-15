package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.PublicNewsController;
import com.hatterscraft.hatters_backend.dto.NewsDTO;
import com.hatterscraft.hatters_backend.model.News;
import com.hatterscraft.hatters_backend.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class PublicNewsControllerTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private PublicNewsController controller;

    private MockMvc mockMvc;

    private News news;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        news = new News();
        news.setId(1L);
        news.setTitle("Test News");
        news.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllNews_shouldReturnList() throws Exception {
        when(newsRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(news));

        mockMvc.perform(get("/public/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test News")));
    }
}
