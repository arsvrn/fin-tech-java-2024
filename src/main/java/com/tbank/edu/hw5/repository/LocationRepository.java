package com.tbank.edu.hw5.repository;

import com.tbank.edu.hw5.model.Location;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LocationRepository implements ILocationRepository {
    private final Map<String, Location> locations = new ConcurrentHashMap<>();

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations.values());
    }

    @Override
    public Optional<Location> findBySlug(String slug) {
        return Optional.ofNullable(locations.get(slug));
    }

    @Override
    public void save(Location location) {
        locations.put(location.getSlug(), location);
    }

    @Override
    public void deleteBySlug(String slug) {
        locations.remove(slug);
    }
}