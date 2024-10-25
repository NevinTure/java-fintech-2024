package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String slug;
    private String language;
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Event> events;

    public Location(String name, String slug, String language) {
        this.name = name;
        this.slug = slug;
        this.language = language;
    }
}
