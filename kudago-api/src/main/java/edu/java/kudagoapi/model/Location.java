package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "events")
@ToString(exclude = "events")
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String slug;
    private String language;
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Event> events;

    public Location(Long id, String name, String slug, String language) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.language = language;
    }

    public Location(String name, String slug, String language) {
        this.name = name;
        this.slug = slug;
        this.language = language;
    }
}
