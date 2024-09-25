package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.repository.ICategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public Optional<Category> updateCategory(int id, Category category) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.save(category);
            return Optional.of(category);
        }
        return Optional.empty();
    }

    public boolean deleteCategory(int id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}