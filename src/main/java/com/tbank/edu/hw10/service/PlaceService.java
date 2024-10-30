package com.tbank.edu.hw10.service;

import com.tbank.edu.hw10.entity.Place;
import com.tbank.edu.hw10.repository.PlaceRepository;
import com.tbank.edu.hw11.observer.Observable;
import com.tbank.edu.hw11.observer.Observer;
import com.tbank.edu.hw11.observer.PlaceSaveObserver;
import com.tbank.edu.hw11.repository.PlaceSnapshotRepository;
import com.tbank.edu.hw11.snapshot.PlaceSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PlaceService implements Observable<Place> {

    @Autowired
    private PlaceRepository locationRepository;
    private final PlaceSnapshotRepository snapshotRepository;
    private final List<Observer<Place>> observers = new ArrayList<>();

    public PlaceService(PlaceSnapshotRepository snapshotRepository,
                        PlaceSaveObserver saveObserver) {
        this.snapshotRepository = snapshotRepository;
        addObserver(saveObserver);
    }

    @Override
    public void addObserver(Observer<Place> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Place> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Place place) {
        for (Observer<Place> observer : observers) {
            observer.update(place);
        }
    }

    public List<Place> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Place> getLocationBySlug(String slug) {
        return locationRepository.findBySlug(slug);
    }

    public Place createLocation(Place place) {
        locationRepository.save(place);
        notifyObservers(place);
        return place;
    }

    public Optional<Place> updateLocation(String slug, Place updatedPlace) {
        return locationRepository.findBySlug(slug).map(existingPlace -> {
            saveSnapshot(existingPlace);
            existingPlace.setName(updatedPlace.getName());
            existingPlace.setSlug(updatedPlace.getSlug());
            locationRepository.save(existingPlace);
            notifyObservers(existingPlace);
            return existingPlace;
        });
    }

    public boolean deleteLocation(String slug) {
        return locationRepository.findBySlug(slug).map(existingPlace -> {
            saveSnapshot(existingPlace);
            locationRepository.delete(existingPlace);
            notifyObservers(existingPlace);
            return true;
        }).orElse(false);
    }

    private void saveSnapshot(Place place) {
        PlaceSnapshot snapshot = new PlaceSnapshot(
                place.getId(),
                place.getSlug(),
                place.getName(),
                LocalDateTime.now()
        );
        snapshotRepository.save(snapshot);
    }

    public List<PlaceSnapshot> getLocationHistory(Long placeId) {
        return snapshotRepository.findByPlaceIdOrderBySnapshotTimeDesc(placeId);
    }
}
