package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepositoryImpl locationRepository;

    public LocationService(LocationRepositoryImpl locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationBySlug(String slug) {
        return locationRepository.findBy(slug);
    }

    public Location createLocation(Location location) {
        locationRepository.save(location);
        return location;
    }

    public Optional<Location> updateLocation(String slug, Location location) {
        if (locationRepository.findBy(slug).isPresent()) {
            locationRepository.save(location);
            return Optional.of(location);
        }
        return Optional.empty();
    }

    public boolean deleteLocation(String slug) {
        Optional<Location> existingLocation = locationRepository.findBy(slug);
        if (existingLocation.isPresent()) {
            locationRepository.deleteBy(slug);
            return true;
        }
        return false;
    }
}