package com.tbank.edu.hw5.init;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ExternalApiService externalApiService;
    private final CategoryRepositoryImpl categoryRepository;
    private final LocationRepositoryImpl locationRepository;

    @Autowired
    public DataInitializer(ExternalApiService externalApiService, CategoryRepositoryImpl categoryRepository, LocationRepositoryImpl locationRepository) {
        this.externalApiService = externalApiService;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Начало инициализации данных из API KudaGo...");

        logger.info("Запрос категорий...");
        List<Category> categories = externalApiService.fetchCategoriesOrNull();
        logger.info("Получено категорий: {}", categories.size());
        categoryRepository.save(categories);
        logger.info("Запрос локаций...");
        List<Location> locations = externalApiService.fetchLocationsOrNull();
        logger.info("Получено локаций: {}", locations.size());
        locationRepository.save(locations);
        logger.info("Инициализация данных завершена.");
    }
}
