package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.PublicEventsController;
import com.hatterscraft.hatters_backend.dto.EventDTO;
import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.repository.EventRepository;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class PublicEventsControllerTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private PublicEventsController controller;

    private MockMvc mockMvc;

    private Event event;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setEventStart(LocalDateTime.now().minusDays(1));
        event.setEventEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    void getCurrentEvent_shouldReturnEventDTO() throws Exception {
        when(eventRepository.findCurrentEvent(any())).thenReturn(Optional.of(event));

        mockMvc.perform(get("/public/events/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Event")));
    }

    @Test
    void getPastEvents_shouldReturnList() throws Exception {
        when(eventRepository.findByEventEndBefore(any())).thenReturn(List.of(event));

        mockMvc.perform(get("/public/events/past"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getFutureEvents_shouldReturnList() throws Exception {
        when(eventRepository.findByEventStartAfter(any())).thenReturn(List.of(event));

        mockMvc.perform(get("/public/events/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getAllEvents_shouldReturnList() throws Exception {
        when(eventRepository.findAll()).thenReturn(List.of(event));

        mockMvc.perform(get("/public/events/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getLatestEvents_shouldReturnList() throws Exception {
        when(eventRepository.findTop3ByOrderByEventStartDesc()).thenReturn(List.of(event));

        mockMvc.perform(get("/public/events/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
