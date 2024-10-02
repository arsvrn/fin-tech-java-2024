package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepositoryImpl categoryRepository;

    public CategoryService(CategoryRepositoryImpl categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findBy(id);
    }

    public Category createCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public Optional<Category> updateCategory(int id, Category category) {
        if (categoryRepository.findBy(id).isPresent()) {
            categoryRepository.save(category);
            return Optional.of(category);
        }
        return Optional.empty();
    }

    public boolean deleteCategory(int id) {
        Optional<Category> existingCategory = categoryRepository.findBy(id);
        if (existingCategory.isPresent()) {
            categoryRepository.deleteBy(id);
            return true;
        }
        return false;
    }
}