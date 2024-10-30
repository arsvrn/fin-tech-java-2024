package com.tbank.edu.hw11.command;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InitializeCategoriesCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(InitializeCategoriesCommand.class);

    private final ExternalApiService externalApiService;
    private final CategoryRepositoryImpl categoryRepository;

    public InitializeCategoriesCommand(ExternalApiService externalApiService, CategoryRepositoryImpl categoryRepository) {
        this.externalApiService = externalApiService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void execute() {
        logger.info("Запрос категорий...");
        List<Category> categories = externalApiService.fetchCategoriesOrNull();
        if (categories != null) {
            logger.info("Получено категорий: {}", categories.size());
            categoryRepository.save(categories);
        } else {
            logger.warn("Не удалось получить категории.");
        }
    }
}
