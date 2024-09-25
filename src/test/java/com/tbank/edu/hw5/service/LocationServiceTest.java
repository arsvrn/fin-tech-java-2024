package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.ILocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    private ILocationRepository locationRepository;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        locationRepository = mock(ILocationRepository.class);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void getAllLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("slug1", "Location 1"));
        locations.add(new Location("slug2", "Location 2"));

        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        assertEquals("Location 1", result.get(0).getName());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void getLocationBySlug() {
        Location location = new Location("slug1", "Location 1");
        when(locationRepository.findBySlug("slug1")).thenReturn(Optional.of(location));

        Optional<Location> result = locationService.getLocationBySlug("slug1");

        assertTrue(result.isPresent());
        assertEquals("Location 1", result.get().getName());
    }

    @Test
    void createLocation() {
        Location location = new Location("slug1", "Location 1");

        locationService.createLocation(location);

        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void updateLocation() {
        Location existingLocation = new Location("slug1", "Location 1");
        Location updatedLocation = new Location("slug1", "Updated Location");

        when(locationRepository.findBySlug("slug1")).thenReturn(Optional.of(existingLocation));

        Optional<Location> result = locationService.updateLocation("slug1", updatedLocation);

        assertTrue(result.isPresent());
        assertEquals("Updated Location", result.get().getName());
        verify(locationRepository, times(1)).save(updatedLocation);
    }

    @Test
    void deleteLocation() {
        when(locationRepository.findBySlug("slug1")).thenReturn(Optional.of(new Location("slug1", "Location 1")));

        boolean result = locationService.deleteLocation("slug1");

        assertTrue(result);
        verify(locationRepository, times(1)).deleteBySlug("slug1");
    }

    @Test
    void deleteLocation_NotFound() {
        when(locationRepository.findBySlug("slug1")).thenReturn(Optional.empty());

        boolean result = locationService.deleteLocation("slug1");

        assertFalse(result);
        verify(locationRepository, times(0)).deleteBySlug("slug1");
    }
}