package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CategoryRepositoryImpl extends AbstractCrudRepository<Category, Integer> {
    private final Map<Integer, Category> categories = new ConcurrentHashMap<>();

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Optional<Category> findBy(Integer id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public void save(Category category) {
        categories.put(category.getId(), category);
    }

    @Override
    public void deleteBy(Integer id) {
        categories.remove(id);
    }
}