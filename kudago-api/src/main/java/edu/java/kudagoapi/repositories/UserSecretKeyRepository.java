package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.UserSecretKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSecretKeyRepository extends JpaRepository<UserSecretKey, Long> {

    Optional<UserSecretKey> findByUserId(Long id);
    boolean existsByUserId(Long id);
}
