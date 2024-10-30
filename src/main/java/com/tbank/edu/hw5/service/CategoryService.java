package com.tbank.edu.hw5.service;

import com.tbank.edu.hw11.observer.CategorySaveObserver;
import com.tbank.edu.hw11.observer.Observable;
import com.tbank.edu.hw11.observer.Observer;
import com.tbank.edu.hw11.repository.CategorySnapshotRepository;
import com.tbank.edu.hw11.snapshot.CategorySnapshot;
import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.repository.CategoryRepositoryImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService implements Observable<Category> {
    private final CategoryRepositoryImpl categoryRepository;
    private final CategorySnapshotRepository snapshotRepository;
    private final List<Observer<Category>> observers = new ArrayList<>();

    public CategoryService(CategoryRepositoryImpl categoryRepository,
                           CategorySnapshotRepository snapshotRepository,
                           Observer<Category> saveObserver) {
        this.categoryRepository = categoryRepository;
        this.snapshotRepository = snapshotRepository;
        addObserver(saveObserver);
    }

    @Override
    public void addObserver(Observer<Category> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Category> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Category category) {
        for (Observer<Category> observer : observers) {
            observer.update(category);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findBy(id);
    }

    public Category createCategory(Category category) {
        categoryRepository.save(category);
        notifyObservers(category);
        return category;
    }

    public Optional<Category> updateCategory(int id, Category category) {
        if (categoryRepository.findBy(id).isPresent()) {
            Category existingCategory = categoryRepository.findBy(id).get();
            saveSnapshot(existingCategory);
            categoryRepository.save(category);
            notifyObservers(category);
            return Optional.of(category);
        }
        return Optional.empty();
    }

    public boolean deleteCategory(int id) {
        Optional<Category> existingCategory = categoryRepository.findBy(id);
        if (existingCategory.isPresent()) {
            saveSnapshot(existingCategory.get());
            categoryRepository.deleteBy(id);
            notifyObservers(existingCategory.get());
            return true;
        }
        return false;
    }

    private void saveSnapshot(Category category) {
        CategorySnapshot snapshot = new CategorySnapshot(
                category.getId(),
                category.getSlug(),
                category.getName(),
                LocalDateTime.now()
        );
        snapshotRepository.save(snapshot);
    }

    public List<CategorySnapshot> getCategoryHistory(int categoryId) {
        return snapshotRepository.findByCategoryIdOrderBySnapshotTimeDesc(categoryId);
    }
}