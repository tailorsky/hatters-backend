package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.EventController;
import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService service;

    @InjectMocks
    private EventController controller;

    @Test
    void getAll_shouldReturnEvents() {
        Event e = new Event();
        when(service.getAll()).thenReturn(List.of(e));
        var events = controller.getAll();
        assertEquals(1, events.size());
    }

    @Test
    void create_shouldReturnEvent() {
        Event e = new Event();
        when(service.create(e)).thenReturn(e);
        assertEquals(e, controller.create(e));
    }

    @Test
    void delete_shouldCallService() {
        doNothing().when(service).delete(1L);
        controller.delete(1L);
        verify(service, times(1)).delete(1L);
    }
}
