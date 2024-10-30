package com.tbank.edu.hw11.observer;

import com.tbank.edu.hw11.repository.CategorySnapshotRepository;
import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw11.snapshot.CategorySnapshot;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategorySaveObserver implements Observer<Category> {

    private final CategorySnapshotRepository snapshotRepository;

    public CategorySaveObserver(CategorySnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public void update(Category category) {
        CategorySnapshot snapshot = new CategorySnapshot(
                category.getId(),
                category.getSlug(),
                category.getName(),
                LocalDateTime.now()
        );
        snapshotRepository.save(snapshot);
    }
}
