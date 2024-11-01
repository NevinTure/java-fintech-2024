package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.LocationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaLocationSnapshotRepository extends JpaRepository<LocationSnapshot, Long> {

    Optional<LocationSnapshot> findFirstByOriginIdOrderByChangeAtDesc(Long id);
}
