package com.tbank.edu.hw11.observer;

import com.tbank.edu.hw10.entity.Place;
import com.tbank.edu.hw11.repository.PlaceSnapshotRepository;
import com.tbank.edu.hw11.snapshot.PlaceSnapshot;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PlaceSaveObserver implements Observer<Place> {

    private final PlaceSnapshotRepository snapshotRepository;

    public PlaceSaveObserver(PlaceSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public void update(Place place) {
        PlaceSnapshot snapshot = new PlaceSnapshot(
                place.getId(),
                place.getSlug(),
                place.getName(),
                LocalDateTime.now()
        );
        snapshotRepository.save(snapshot);
    }
}

