package com.tbank.edu.hw10.controller;

import com.tbank.edu.hw10.entity.Event;
import com.tbank.edu.hw10.exception.BadRequestException;
import com.tbank.edu.hw10.exception.NotFoundException;
import com.tbank.edu.hw10.service.EventServiceDB;
import com.tbank.edu.hw10.service.PlaceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/events")
public class EventControllerDB {

    @Autowired
    private EventServiceDB eventServiceDB;

    @Autowired
    private PlaceService locationService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventServiceDB.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventServiceDB.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        locationService.getLocationBySlug(event.getPlace().getSlug())
                .orElseThrow(() -> new BadRequestException("Город с slug " + event.getPlace().getSlug() + " не существует"));

        Event createdEvent = eventServiceDB.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        return eventServiceDB.updateEvent(id, event)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventServiceDB.deleteEvent(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Событие с id " + id + " не найдено");
        }
    }
}
