package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.UserSecretKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserSecretKeyRepository extends JpaRepository<UserSecretKey, Long> {

    Optional<UserSecretKey> findByUserId(Long id);
}
