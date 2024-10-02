package com.tbank.edu.hw7.service;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import com.tbank.edu.hw5.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepositoryImpl categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepositoryImpl.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "slug 1", "Category 1"));
        categories.add(new Category(2, "slug 2", "Category 2"));

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryBy() {
        Category category = new Category(1, "slug 1", "Category 1");
        when(categoryRepository.findBy(1)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1);

        assertTrue(result.isPresent());
        assertEquals("Category 1", result.get().getName());
    }

    @Test
    void createCategory() {
        Category category = new Category(1, "slug 1", "Category 1");

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Category 1", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory() {
        Category existingCategory = new Category(1, "slug 1", "Category 1");
        Category updatedCategory = new Category(1, "slug update", "Updated Category");

        when(categoryRepository.findBy(1)).thenReturn(Optional.of(existingCategory));

        Optional<Category> result = categoryService.updateCategory(1, updatedCategory);

        assertTrue(result.isPresent());
        assertEquals("Updated Category", result.get().getName());
        verify(categoryRepository, times(1)).save(updatedCategory);
    }

    @Test
    void deleteCategory() {
        when(categoryRepository.findBy(1)).thenReturn(Optional.of(new Category(1, "slug 1", "Category 1")));

        boolean result = categoryService.deleteCategory(1);

        assertTrue(result);
        verify(categoryRepository, times(1)).deleteBy(1);
    }

    @Test
    void deleteCategory_NotFound() {
        when(categoryRepository.findBy(1)).thenReturn(Optional.empty());

        boolean result = categoryService.deleteCategory(1);

        assertFalse(result);
        verify(categoryRepository, times(0)).deleteBy(1);
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryRepository.findBy(1)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(1);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findBy(1);
    }

    @Test
    void updateCategory_NotFound() {
        Category updatedCategory = new Category(1, "slug update", "Updated Category");

        when(categoryRepository.findBy(1)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.updateCategory(1, updatedCategory);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(0)).save(updatedCategory);
    }

}