package com.tbank.edu.hw10.repository;

import com.tbank.edu.hw10.entity.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventSpecificationRepository {

    List<Event> findAll(Specification<Event> specification);
}
