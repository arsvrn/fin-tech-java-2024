package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.ILocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final ILocationRepository locationRepository;

    public LocationService(ILocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationBySlug(String slug) {
        return locationRepository.findBySlug(slug);
    }

    public Location createLocation(Location location) {
        locationRepository.save(location);
        return location;
    }

    public Optional<Location> updateLocation(String slug, Location location) {
        if (locationRepository.findBySlug(slug).isPresent()) {
            locationRepository.save(location);
            return Optional.of(location);
        }
        return Optional.empty();
    }

    public boolean deleteLocation(String slug) {
        Optional<Location> existingLocation = locationRepository.findBySlug(slug);
        if (existingLocation.isPresent()) {
            locationRepository.deleteBySlug(slug);
            return true;
        }
        return false;
    }
}