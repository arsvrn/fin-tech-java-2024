package com.tbank.edu.hw11.snapshot;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "category_snapshots")
@NoArgsConstructor
public class CategorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapshotId;

    private int categoryId;
    private String slug;
    private String name;
    private LocalDateTime snapshotTime;

    public CategorySnapshot(int categoryId, String slug, String name, LocalDateTime snapshotTime) {
        this.categoryId = categoryId;
        this.slug = slug;
        this.name = name;
        this.snapshotTime = snapshotTime;
    }

}
