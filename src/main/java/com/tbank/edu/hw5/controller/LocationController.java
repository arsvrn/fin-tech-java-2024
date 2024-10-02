package com.tbank.edu.hw5.controller;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tbank.edu.hw5.aspect.LogExecutionTime;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @com.tbank.edu.hw5.aspect.LogExecutionTime
    @GetMapping("/{slug}")
    public ResponseEntity<Location> getLocationBySlug(@PathVariable String slug) {
        Optional<Location> location = locationService.getLocationBySlug(slug);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Location> updateLocation(@PathVariable String slug, @RequestBody Location location) {
        Optional<Location> updatedLocation = locationService.updateLocation(slug, location);
        return updatedLocation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String slug) {
        if (locationService.deleteLocation(slug)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
