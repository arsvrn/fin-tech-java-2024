package com.tbank.edu.hw11.repository;

import com.tbank.edu.hw11.snapshot.PlaceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceSnapshotRepository extends JpaRepository<PlaceSnapshot, String> {
    List<PlaceSnapshot> findByPlaceIdOrderBySnapshotTimeDesc(Long placeId);
}
