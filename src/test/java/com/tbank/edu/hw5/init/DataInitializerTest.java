package com.tbank.edu.hw5.init;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class DataInitializerTest {

    private ExternalApiService externalApiService;
    private CategoryRepositoryImpl categoryRepository;
    private LocationRepositoryImpl locationRepository;
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setUp() {
        externalApiService = mock(ExternalApiService.class);
        categoryRepository = mock(CategoryRepositoryImpl.class);
        locationRepository = mock(LocationRepositoryImpl.class);
        dataInitializer = new DataInitializer(externalApiService, categoryRepository, locationRepository);
    }

    @Test
    public void testRun_initializesDataFromApi() {
        Category category1 = new Category(1,"Slug 1", "Category1");
        Category category2 = new Category(2,"Slug 2", "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        Location location1 = new Location("Slug 1", "Location1");
        Location location2 = new Location("Slug 2", "Location2");
        List<Location> locations = Arrays.asList(location1, location2);

        when(externalApiService.fetchCategoriesOrNull()).thenReturn(categories);
        when(externalApiService.fetchLocationsOrNull()).thenReturn(locations);

        dataInitializer.run();

        verify(externalApiService).fetchCategoriesOrNull();
        verify(categoryRepository).save(categories);
        verify(externalApiService).fetchLocationsOrNull();
        verify(locationRepository).save(locations);
    }

    @Test
    public void testRun_whenNoCategories_returnsEmptyList() {
        when(externalApiService.fetchCategoriesOrNull()).thenReturn(List.of());

        dataInitializer.run();

        verify(categoryRepository).save(List.of());
    }

    @Test
    public void testRun_whenNoLocations_returnsEmptyList() {
        when(externalApiService.fetchLocationsOrNull()).thenReturn(List.of());

        dataInitializer.run();

        verify(locationRepository).save(List.of());
    }
}