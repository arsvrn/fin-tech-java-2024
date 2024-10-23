package com.tbank.edu.hw10.repository;

import com.tbank.edu.hw10.entity.Event;
import com.tbank.edu.hw10.entity.Place;

import java.time.LocalDate;
import java.util.List;

public interface EventSpecificationRepository {

    List<Event> findByCriteria(String name, Place place, LocalDate fromDate, LocalDate toDate);
}
