package com.tbank.edu.hw11.command;

import com.tbank.edu.hw5.model.Location;
import com.tbank.edu.hw5.repository.LocationRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InitializeLocationsCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(InitializeLocationsCommand.class);

    private final ExternalApiService externalApiService;
    private final LocationRepositoryImpl locationRepository;

    public InitializeLocationsCommand(ExternalApiService externalApiService, LocationRepositoryImpl locationRepository) {
        this.externalApiService = externalApiService;
        this.locationRepository = locationRepository;
    }

    @Override
    public void execute() {
        logger.info("Запрос локаций...");
        List<Location> locations = externalApiService.fetchLocationsOrNull();
        if (locations != null) {
            logger.info("Получено локаций: {}", locations.size());
            locationRepository.save(locations);
        } else {
            logger.warn("Не удалось получить локации.");
        }
    }
}
