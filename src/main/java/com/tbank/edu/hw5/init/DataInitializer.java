package com.tbank.edu.hw5.init;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ExternalApiService externalApiService;
    private final CategoryRepositoryImpl categoryRepository;
    private final LocationRepositoryImpl locationRepository;
    private final ExecutorService dataInitializerExecutor;
    private final ScheduledExecutorService scheduledTaskExecutor;

    @Value("${data.initialization.interval}")
    private Duration initializationInterval;

    @Autowired
    public DataInitializer(
            ExternalApiService externalApiService,
            CategoryRepositoryImpl categoryRepository,
            LocationRepositoryImpl locationRepository,
            @Qualifier("dataInitializerExecutor") ExecutorService dataInitializerExecutor,
            @Qualifier("scheduledTaskExecutor") ScheduledExecutorService scheduledTaskExecutor) {
        this.externalApiService = externalApiService;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.dataInitializerExecutor = dataInitializerExecutor;
        this.scheduledTaskExecutor = scheduledTaskExecutor;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted() {
        logger.info("Приложение запущено. Настройка планировщика задач...");

        scheduledTaskExecutor.scheduleAtFixedRate(
                this::initializeData,
                0,
                initializationInterval.toMinutes(),
                TimeUnit.MINUTES
        );
    }

    public void initializeData() {
        logger.info("Запуск параллельной инициализации данных...");
        long startTime = System.nanoTime();

        List<Callable<Void>> tasks = Arrays.asList(
                () -> {
                    logger.info("Запрос категорий...");
                    List<Category> categories = externalApiService.fetchCategoriesOrNull();
                    logger.info("Получено категорий: {}", categories.size());
                    categoryRepository.save(categories);
                    return null;
                },
                () -> {
                    logger.info("Запрос локаций...");
                    List<Location> locations = externalApiService.fetchLocationsOrNull();
                    logger.info("Получено локаций: {}", locations.size());
                    locationRepository.save(locations);
                    return null;
                }
        );

        try {
            List<Future<Void>> futures = dataInitializerExecutor.invokeAll(tasks);

            for (Future<Void> future : futures) {
                future.get();
            }

            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

            logger.info("Инициализация данных завершена успешно за {} мс", duration);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка во время инициализации данных: ", e);
            Thread.currentThread().interrupt();
        }
    }
}