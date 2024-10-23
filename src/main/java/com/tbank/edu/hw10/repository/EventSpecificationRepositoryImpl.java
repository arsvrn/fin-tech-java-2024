package com.tbank.edu.hw10.repository;

import com.tbank.edu.hw10.entity.Event;
import com.tbank.edu.hw10.entity.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventSpecificationRepositoryImpl implements EventSpecificationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findByCriteria(String name, Place place, LocalDate fromDate, LocalDate toDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(eventRoot.get("name"), "%" + name + "%"));
        }

        if (place != null) {
            predicates.add(cb.equal(eventRoot.get("place"), place));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("date"), fromDate));
        }

        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(eventRoot.get("date"), toDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        query.distinct(true);

        return entityManager.createQuery(query).getResultList();
    }
}
