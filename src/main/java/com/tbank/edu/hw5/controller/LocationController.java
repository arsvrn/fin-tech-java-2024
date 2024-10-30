package com.tbank.edu.hw5.controller;

import com.tbank.edu.hw10.entity.Place;
import com.tbank.edu.hw10.exception.NotFoundException;
import com.tbank.edu.hw10.service.PlaceService;
import com.tbank.edu.hw11.snapshot.PlaceSnapshot;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    @Autowired
    private PlaceService locationService;

    @GetMapping
    public ResponseEntity<List<Place>> getAllLocations() {
        List<Place> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Place> getLocationBySlug(@PathVariable String slug) {
        return locationService.getLocationBySlug(slug)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Город с slug " + slug + " не найден"));
    }

    @PostMapping
    public ResponseEntity<Place> createLocation(@Valid @RequestBody Place Place) {
        Place createdLocation = locationService.createLocation(Place);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Place> updateLocation(@PathVariable String slug, @Valid @RequestBody Place Place) {
        Optional<Place> updatedLocation = locationService.updateLocation(slug, Place);
        return updatedLocation.map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Город с slug " + slug + " не найден"));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String slug) {
        if (locationService.deleteLocation(slug)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Город с slug " + slug + " не найден");
        }
    }

    @GetMapping("/{slug}/history")
    public ResponseEntity<List<PlaceSnapshot>> getLocationHistory(@PathVariable Long placeId) {
        List<PlaceSnapshot> history = locationService.getLocationHistory(placeId);
        return ResponseEntity.ok(history);
    }
}