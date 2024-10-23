package com.tbank.edu.hw10.service;

import com.tbank.edu.hw10.entity.Event;
import com.tbank.edu.hw10.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceDB {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> updateEvent(Long id, Event event) {
        return eventRepository.findById(id).map(existingEvent -> {
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            existingEvent.setPlace(event.getPlace());
            return eventRepository.save(existingEvent);
        });
    }

    public boolean deleteEvent(Long id) {
        return eventRepository.findById(id).map(event -> {
            eventRepository.delete(event);
            return true;
        }).orElse(false);
    }
}

