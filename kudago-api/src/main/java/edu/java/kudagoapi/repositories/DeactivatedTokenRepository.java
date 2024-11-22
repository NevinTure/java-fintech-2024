package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {
}
