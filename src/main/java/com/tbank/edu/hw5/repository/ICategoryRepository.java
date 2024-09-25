package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(int id);
    void save(Category category);
    void deleteById(int id);
}
