package com.tbank.edu.hw5.init;
import com.tbank.edu.hw11.command.Command;
import com.tbank.edu.hw11.command.InitializeCategoriesCommand;
import com.tbank.edu.hw11.command.InitializeLocationsCommand;
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

    private final List<Command> commands;
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

        this.dataInitializerExecutor = dataInitializerExecutor;
        this.scheduledTaskExecutor = scheduledTaskExecutor;

        this.commands = Arrays.asList(
                new InitializeCategoriesCommand(externalApiService, categoryRepository),
                new InitializeLocationsCommand(externalApiService, locationRepository)
        );
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

        List<Callable<Void>> tasks = commands.stream()
                .map(command -> (Callable<Void>) () -> {
                    command.execute();
                    return null;
                })
                .toList();

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
