package com.tbank.edu.hw5.controller;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    public LocationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLocations() {
        when(locationService.getAllLocations()).thenReturn(Arrays.asList(
                new Location("msk", "Москва"),
                new Location("spb", "Санкт-Петербург")
        ));

        ResponseEntity<?> response = locationController.getAllLocations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((Iterable<Location>) response.getBody()).spliterator().getExactSizeIfKnown());
    }

    @Test
    void testGetLocationBySlug() {
        when(locationService.getLocationBySlug("msk")).thenReturn(Optional.of(new Location("msk", "Москва")));

        ResponseEntity<?> response = locationController.getLocationBySlug("msk");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Москва", ((Location) response.getBody()).getName());
    }

    @Test
    void testCreateLocation() {
        Location newLocation = new Location("new-city", "Новый город");
        when(locationService.createLocation(any(Location.class))).thenReturn(new Location("new-city", "Новый город"));

        ResponseEntity<?> response = locationController.createLocation(newLocation);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Новый город", ((Location) response.getBody()).getName());
    }

    @Test
    void testUpdateLocation() {
        Location updatedLocation = new Location("msk", "Обновленная Москва");
        when(locationService.updateLocation(eq("msk"), any(Location.class))).thenReturn(Optional.of(updatedLocation));

        ResponseEntity<?> response = locationController.updateLocation("msk", updatedLocation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Обновленная Москва", ((Location) response.getBody()).getName());
    }

    @Test
    void testDeleteLocation() {
        when(locationService.deleteLocation("msk")).thenReturn(true);
        ResponseEntity<?> response = locationController.deleteLocation("msk");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(locationService, times(1)).deleteLocation("msk");
    }
}