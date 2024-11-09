package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Entity
@Table(name = "user_secret_key")
public class UserSecretKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String secret;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public UserSecretKey() {
        createdAt = OffsetDateTime.now().withNano(0);
        updatedAt = OffsetDateTime.now().withNano(0);
    }

    public UserSecretKey(String secret) {
        this();
        this.secret = secret;
    }
}
