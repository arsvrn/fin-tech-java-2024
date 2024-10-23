package com.tbank.edu.hw5.controller;

import com.tbank.edu.hw10.entity.Place;
import com.tbank.edu.hw10.exception.NotFoundException;
import com.tbank.edu.hw10.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @Mock
    private PlaceService locationService;

    @InjectMocks
    private LocationController locationController;

    private Place Place;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Place = new Place();
        Place.setSlug("msk");
        Place.setName("Москва");
    }

    @Test
    void testGetAllLocations() {
        List<Place> locations = Arrays.asList(Place);
        when(locationService.getAllLocations()).thenReturn(locations);

        ResponseEntity<List<Place>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(locationService, times(1)).getAllLocations();
    }

    // Тест получения города по slug, когда город найден
    @Test
    void testGetLocationBySlug_Found() {
        when(locationService.getLocationBySlug("msk")).thenReturn(Optional.of(Place));

        ResponseEntity<Place> response = locationController.getLocationBySlug("msk");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Москва", response.getBody().getName());
        verify(locationService, times(1)).getLocationBySlug("msk");
    }

    // Тест получения города по slug, когда город не найден
    @Test
    void testGetLocationBySlug_NotFound() {
        when(locationService.getLocationBySlug("msk")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            locationController.getLocationBySlug("msk");
        });

        assertEquals("Город с slug msk не найден", exception.getMessage());
        verify(locationService, times(1)).getLocationBySlug("msk");
    }

    // Тест создания нового города
    @Test
    void testCreateLocation() {
        when(locationService.createLocation(any(Place.class))).thenReturn(Place);

        ResponseEntity<Place> response = locationController.createLocation(Place);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Москва", response.getBody().getName());
        verify(locationService, times(1)).createLocation(any(Place.class));
    }

    // Тест обновления города, когда город найден
    @Test
    void testUpdateLocation_Found() {
        when(locationService.updateLocation(eq("msk"), any(Place.class))).thenReturn(Optional.of(Place));

        ResponseEntity<Place> response = locationController.updateLocation("msk", Place);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Москва", response.getBody().getName());
        verify(locationService, times(1)).updateLocation(eq("msk"), any(Place.class));
    }

    // Тест обновления города, когда город не найден
    @Test
    void testUpdateLocation_NotFound() {
        when(locationService.updateLocation(eq("msk"), any(Place.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            locationController.updateLocation("msk", Place);
        });

        assertEquals("Город с slug msk не найден", exception.getMessage());
        verify(locationService, times(1)).updateLocation(eq("msk"), any(Place.class));
    }

    // Тест удаления города, когда город найден
    @Test
    void testDeleteLocation_Found() {
        when(locationService.deleteLocation("msk")).thenReturn(true);

        ResponseEntity<Void> response = locationController.deleteLocation("msk");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(locationService, times(1)).deleteLocation("msk");
    }

    // Тест удаления города, когда город не найден
    @Test
    void testDeleteLocation_NotFound() {
        when(locationService.deleteLocation("msk")).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            locationController.deleteLocation("msk");
        });

        assertEquals("Город с slug msk не найден", exception.getMessage());
        verify(locationService, times(1)).deleteLocation("msk");
    }
}