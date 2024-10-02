package com.tbank.edu.hw5.repository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID>{
    public abstract List<T> findAll();
    public abstract Optional<T> findBy(ID id);
    public abstract void save(T entity);
    public abstract void deleteBy(ID id);

    public void save(List<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }
}
