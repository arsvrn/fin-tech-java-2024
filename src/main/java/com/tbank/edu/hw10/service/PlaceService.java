package com.tbank.edu.hw10.service;

import com.tbank.edu.hw10.entity.Place;
import com.tbank.edu.hw10.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository locationRepository;

    public List<Place> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Place> getLocationBySlug(String slug) {
        return locationRepository.findBySlug(slug);
    }

    public Place createLocation(Place location) {
        return locationRepository.save(location);
    }

    public Optional<Place> updateLocation(String slug, Place location) {
        return locationRepository.findBySlug(slug).map(existingLocation -> {
            existingLocation.setName(location.getName());
            existingLocation.setSlug(location.getSlug());
            return locationRepository.save(existingLocation);
        });
    }

    public boolean deleteLocation(String slug) {
        return locationRepository.findBySlug(slug).map(location -> {
            locationRepository.delete(location);
            return true;
        }).orElse(false);
    }
}