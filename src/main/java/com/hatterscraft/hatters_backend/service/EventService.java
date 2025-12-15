package com.hatterscraft.hatters_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.repository.EventRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository repo;

    public Event create(Event e){
        return repo.save(e);
    }

    public Event update(Long id, Event e){
        Event existing = repo.findById(id).orElseThrow();
        existing.setTitle(e.getTitle());
        existing.setDescription(e.getDescription());
        existing.setVideoUrl(e.getVideoUrl());
        existing.setImageUrl(e.getImageUrl());
        existing.setEventStart(e.getEventStart());
        existing.setEventEnd(e.getEventEnd());
        existing.setRewards(e.getRewards());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Event> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "eventStart"));
    }

    public Event getCurrent() {
        return repo.findCurrentEvent(LocalDateTime.now()).orElse(null);
    }

    public List<Event> getPast() {
        return repo.findByEventEndBefore(LocalDateTime.now());
    }

    public List<Event> getUpcoming() {
        return repo.findByEventStartAfter(LocalDateTime.now());
    }
}
