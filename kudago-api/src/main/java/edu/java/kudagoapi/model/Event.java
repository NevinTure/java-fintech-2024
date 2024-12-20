package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "location")
@ToString
@Entity
@NamedEntityGraph(
        name = "event_entity-graph", attributeNodes = @NamedAttributeNode("location")
)
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public Event(String title, String slug, LocalDate date, Location location) {
        this.title = title;
        this.slug = slug;
        this.date = date;
        this.location = location;
    }
}
