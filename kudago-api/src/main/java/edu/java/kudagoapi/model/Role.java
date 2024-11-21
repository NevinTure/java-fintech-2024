package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Role() {
        createdAt = OffsetDateTime.now().withNano(0);
        updatedAt = OffsetDateTime.now().withNano(0);
    }

    public Role(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
