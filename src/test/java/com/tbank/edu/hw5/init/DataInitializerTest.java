package com.tbank.edu.hw5.init;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.*;

public class DataInitializerTest {

    private ExternalApiService externalApiService;
    private CategoryRepositoryImpl categoryRepository;
    private LocationRepositoryImpl locationRepository;
    private ExecutorService dataInitializerExecutor;
    private ScheduledExecutorService scheduledTaskExecutor;
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setUp() {
        externalApiService = mock(ExternalApiService.class);
        categoryRepository = mock(CategoryRepositoryImpl.class);
        locationRepository = mock(LocationRepositoryImpl.class);
        dataInitializerExecutor = mock(ExecutorService.class);
        scheduledTaskExecutor = mock(ScheduledExecutorService.class);
        dataInitializer = new DataInitializer(
                externalApiService,
                categoryRepository,
                locationRepository,
                dataInitializerExecutor,
                scheduledTaskExecutor
        );
    }

    @Test
    public void testRun_initializesDataFromApi() {
        // Создание тестовых данных
        Category category1 = new Category(1, "Slug 1", "Category1");
        Category category2 = new Category(2, "Slug 2", "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        Location location1 = new Location("Slug 1", "Location1");
        Location location2 = new Location("Slug 2", "Location2");
        List<Location> locations = Arrays.asList(location1, location2);


        when(externalApiService.fetchCategoriesOrNull()).thenReturn(categories);
        when(externalApiService.fetchLocationsOrNull()).thenReturn(locations);


        dataInitializer.initializeData();


        verify(dataInitializerExecutor, times(2)).submit(any(Runnable.class));


        verify(externalApiService, never()).fetchCategoriesOrNull();
        verify(externalApiService, never()).fetchLocationsOrNull();


        Runnable categoryTask = captureAndExecuteTask();
        Runnable locationTask = captureAndExecuteTask();

        categoryTask.run();
        locationTask.run();


        verify(externalApiService).fetchCategoriesOrNull();
        verify(categoryRepository).save(categories);
        verify(externalApiService).fetchLocationsOrNull();
        verify(locationRepository).save(locations);
    }

    @Test
    public void testRun_whenNoCategories_returnsEmptyList() {

        when(externalApiService.fetchCategoriesOrNull()).thenReturn(List.of());


        dataInitializer.initializeData();


        Runnable categoryTask = captureAndExecuteTask();
        categoryTask.run();


        verify(categoryRepository).save(List.of());
    }

    @Test
    public void testRun_whenNoLocations_returnsEmptyList() {

        when(externalApiService.fetchLocationsOrNull()).thenReturn(List.of());


        dataInitializer.initializeData();


        Runnable locationTask = captureAndExecuteTask();
        locationTask.run();


        verify(locationRepository).save(List.of());
    }


    private Runnable captureAndExecuteTask() {
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(dataInitializerExecutor).submit(runnableCaptor.capture());
        return runnableCaptor.getValue();
    }
}