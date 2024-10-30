package com.tbank.edu.hw11.snapshot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "place_snapshots")
public class PlaceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long placeId;
    private String slug;
    private String name;
    private LocalDateTime snapshotTime;

    public PlaceSnapshot(Long id, String slug, String name, LocalDateTime now) {
        this.placeId = id;
        this.slug = slug;
        this.name = name;
        this.snapshotTime = now;
    }
}
