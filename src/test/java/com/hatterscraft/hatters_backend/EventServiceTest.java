package com.hatterscraft.hatters_backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.repository.EventRepository;
import com.hatterscraft.hatters_backend.service.EventService;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void create_shouldSaveEvent() {
        Event event = new Event();
        event.setTitle("Test");

        when(eventRepository.save(event)).thenReturn(event);

        Event saved = eventService.create(event);

        assertThat(saved.getTitle()).isEqualTo("Test");
        verify(eventRepository).save(event);
    }

    @Test
    void getCurrent_shouldReturnEvent() {
        Event event = new Event();
        when(eventRepository.findCurrentEvent(any()))
                .thenReturn(Optional.of(event));

        Event current = eventService.getCurrent();

        assertThat(current).isNotNull();
    }

    @Test
    void getCurrent_shouldReturnNullIfEmpty() {
        when(eventRepository.findCurrentEvent(any()))
                .thenReturn(Optional.empty());

        Event current = eventService.getCurrent();

        assertThat(current).isNull();
    }
}

