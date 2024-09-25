package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Location;

import java.util.List;
import java.util.Optional;

public interface ILocationRepository {
    List<Location> findAll();
    Optional<Location> findBySlug(String slug);
    void save(Location location);
    void deleteBySlug(String slug);
}
