package com.tbank.edu.hw5.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    Optional<T> findBy(ID id);
    void save(T entity);
    void deleteBy(ID id);
    void save(List<T> entities);
}