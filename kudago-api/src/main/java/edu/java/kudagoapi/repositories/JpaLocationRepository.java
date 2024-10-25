package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLocationRepository extends JpaRepository<Location, Long> {
}
