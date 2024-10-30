package com.tbank.edu.hw11.repository;

import com.tbank.edu.hw11.snapshot.CategorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorySnapshotRepository extends JpaRepository<CategorySnapshot, Long> {
    List<CategorySnapshot> findByCategoryIdOrderBySnapshotTimeDesc(int categoryId);
}