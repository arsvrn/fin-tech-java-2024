package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {
    private CategoryRepositoryImpl categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = new CategoryRepositoryImpl();
    }

    @Test
    void testFindAll_ReturnsAllCategories() {
        Category category1 = new Category(1, "Slug 1", "Category 1");
        Category category2 = new Category(2, "Slug 2", "Category 2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
    }

    @Test
    void testFindById_ReturnsCategoryWhenExists() {
        Category category = new Category(1, "Slug 1", "Category 1");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findBy(1);

        assertTrue(foundCategory.isPresent());
        assertEquals(category, foundCategory.get());
    }

    @Test
    void testFindById_ReturnsEmptyWhenNotExists() {
        Optional<Category> foundCategory = categoryRepository.findBy(999);

        assertFalse(foundCategory.isPresent());
    }

    @Test
    void testSave_SavesCategorySuccessfully() {
        Category category = new Category(1, "Slug 1", "Category 1");

        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findBy(1);
        assertTrue(foundCategory.isPresent());
        assertEquals(category, foundCategory.get());
    }

    @Test
    void testDeleteById_RemovesCategorySuccessfully() {
        Category category = new Category(1, "Slug 1", "Category 1");
        categoryRepository.save(category);

        categoryRepository.deleteBy(1);

        Optional<Category> foundCategory = categoryRepository.findBy(1);
        assertFalse(foundCategory.isPresent());
    }

    @Test
    void testDeleteById_DoesNothingWhenNotExists() {
        categoryRepository.deleteBy(999);

        List<Category> categories = categoryRepository.findAll();
        assertTrue(categories.isEmpty());
    }
}