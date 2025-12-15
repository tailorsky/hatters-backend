package com.hatterscraft.hatters_backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hatterscraft.hatters_backend.dto.EventDTO;
import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.repository.EventRepository;

@RestController
public class PublicEventsController {

    private final EventRepository eventRepository;

    public PublicEventsController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/public/events/current")
    public EventDTO getCurrentEvent() {
        LocalDateTime now = LocalDateTime.now();
        Event currentEvent = eventRepository.findCurrentEvent(now).orElse(null);

        if (currentEvent == null) return null;

        return new EventDTO(
                currentEvent.getId(),
                currentEvent.getTitle(),
                currentEvent.getDescription(),
                currentEvent.getVideoUrl(),
                currentEvent.getImageUrl(),
                currentEvent.getEventStart(),
                currentEvent.getEventEnd(),
                currentEvent.getRewards()
        );
    }

    @GetMapping("/public/events/past")
    public List<EventDTO> getPastEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByEventEndBefore(now).stream()
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getVideoUrl(),
                        event.getImageUrl(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRewards()
                ))
                .toList();
    }

    @GetMapping("/public/events/upcoming")
    public List<EventDTO> getFutureEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByEventStartAfter(now).stream()
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getVideoUrl(),
                        event.getImageUrl(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRewards()
                ))
                .toList();
    }
    @GetMapping("/public/events/all")
    public List<EventDTO> getAllEvents() {;
        return eventRepository.findAll().stream()
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getVideoUrl(),
                        event.getImageUrl(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRewards()
                ))
                .toList();
    }
    @GetMapping("/public/events/latest")
    public List<EventDTO> getLatestEvents() {
        return eventRepository.findTop3ByOrderByEventStartDesc()
                .stream()
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getVideoUrl(),
                        event.getImageUrl(),
                        event.getEventStart(),
                        event.getEventEnd(),
                        event.getRewards()
                ))
                .toList();
    }
}
