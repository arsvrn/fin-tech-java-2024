package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationRepositoryTest {
    private LocationRepositoryImpl locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository = new LocationRepositoryImpl();
    }

    @Test
    void testFindAll_ReturnsAllLocations() {
        Location location1 = new Location("slug1", "Location 1");
        Location location2 = new Location("slug2", "Location 2");
        locationRepository.save(location1);
        locationRepository.save(location2);

        List<Location> locations = locationRepository.findAll();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location1));
        assertTrue(locations.contains(location2));
    }

    @Test
    void testFindBySlug_ReturnsLocationWhenExists() {
        Location location = new Location("slug1", "Location 1");
        locationRepository.save(location);

        Optional<Location> foundLocation = locationRepository.findBy("slug1");

        assertTrue(foundLocation.isPresent());
        assertEquals(location, foundLocation.get());
    }

    @Test
    void testFindBySlug_ReturnsEmptyWhenNotExists() {
        Optional<Location> foundLocation = locationRepository.findBy("nonexistent_slug");

        assertFalse(foundLocation.isPresent());
    }

    @Test
    void testSave_SavesLocationSuccessfully() {
        Location location = new Location("slug1", "Location 1");

        locationRepository.save(location);

        Optional<Location> foundLocation = locationRepository.findBy("slug1");
        assertTrue(foundLocation.isPresent());
        assertEquals(location, foundLocation.get());
    }

    @Test
    void testDeleteBySlug_RemovesLocationSuccessfully() {
        Location location = new Location("slug1", "Location 1");
        locationRepository.save(location);

        locationRepository.deleteBy("slug1");

        Optional<Location> foundLocation = locationRepository.findBy("slug1");
        assertFalse(foundLocation.isPresent());
    }

    @Test
    void testDeleteBySlug_DoesNothingWhenNotExists() {
        locationRepository.deleteBy("nonexistent_slug");

        List<Location> locations = locationRepository.findAll();
        assertTrue(locations.isEmpty());
    }
}