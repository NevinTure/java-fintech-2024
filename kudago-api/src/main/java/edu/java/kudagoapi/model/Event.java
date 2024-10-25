package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;
}
