package com.hatterscraft.hatters_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.service.EventService;


@RestController
@RequestMapping("/admin/events")
@PreAuthorize("hasRole('ADMIN')")
public class EventController {
    @Autowired
    private EventService service;

    @GetMapping
    public List<Event> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Event create(@RequestBody Event e) {
        return service.create(e);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event e) {
        return service.update(id, e);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/current")
    public Event current() {
        return service.getCurrent();
    }

    @GetMapping("/past")
    public List<Event> past() {
        return service.getPast();
    }

    @GetMapping("/upcoming")
    public List<Event> upcoming() {
        return service.getUpcoming();
    }
    @GetMapping("/all")
    public List<Event> allEvents() {
        return service.getAll();
    }
    
}

