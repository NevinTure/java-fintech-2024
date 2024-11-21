package edu.java.kudagoapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deactivated_token")
public class DeactivatedToken {

    @Id
    private UUID id;
    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}
