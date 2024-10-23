package com.tbank.edu.hw10.repository;

import com.tbank.edu.hw10.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.events WHERE p.id = :id")
    Place findByIdWithEvents(@Param("id") Long id);
    Optional<Place> findBySlug(String slug);
}
