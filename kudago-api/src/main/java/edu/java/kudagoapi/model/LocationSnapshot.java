package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "location_snapshot")
public class LocationSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "slug", unique = true, nullable = false)
    private String slug;
    private String name;
    private String language;
    @Column(name = "change_at")
    private OffsetDateTime changeAt;
    @Column(name = "origin_id")
    private Long originId;

    public LocationSnapshot(String slug, String name, String language) {
        this();
        this.slug = slug;
        this.name = name;
        this.language = language;
    }

    public LocationSnapshot() {
        changeAt = OffsetDateTime.now().withNano(0);
    }
}
