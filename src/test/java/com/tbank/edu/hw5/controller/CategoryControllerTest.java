package com.tbank.edu.hw5.controller;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    public CategoryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(
                new Category(123, "airports", "Аэропорты"),
                new Category(89, "amusement", "Развлечения")
        ));

        ResponseEntity<?> response = categoryController.getAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((Iterable<Category>) response.getBody()).spliterator().getExactSizeIfKnown());
    }

    @Test
    void testGetCategoryById() {
        when(categoryService.getCategoryById(123)).thenReturn(Optional.of(new Category(123, "airports", "Аэропорты")));

        ResponseEntity<?> response = categoryController.getCategoryById(123);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Аэропорты", ((Category) response.getBody()).getName());
    }

    @Test
    void testCreateCategory() {
        Category newCategory = new Category(0, "new-category", "Новая категория");
        when(categoryService.createCategory(any(Category.class))).thenReturn(new Category(999, "new-category", "Новая категория"));

        ResponseEntity<?> response = categoryController.createCategory(newCategory);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Новая категория", ((Category) response.getBody()).getName());
    }

    @Test
    void testUpdateCategory() {
        Category updatedCategory = new Category(123, "updated-category", "Обновленная категория");
        when(categoryService.updateCategory(eq(123), any(Category.class))).thenReturn(Optional.of(updatedCategory));

        ResponseEntity<?> response = categoryController.updateCategory(123, updatedCategory);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Обновленная категория", ((Category) response.getBody()).getName());
    }

    @Test
    void testDeleteCategory() {
        when(categoryService.deleteCategory(123)).thenReturn(true);
        ResponseEntity<?> response = categoryController.deleteCategory(123);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategory(123);
    }
}