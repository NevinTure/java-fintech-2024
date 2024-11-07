package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface JpaLocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findBySlug(String slug);

    List<Location> findAll(Specification<Location> spec);

    static Specification<Location> buildSpecification(Long id, String slug) {
        List<Specification<Location>> specs = new ArrayList<>();
        if (id != null) {
            specs.add((Specification<Location>) (location, query, cb) -> cb.equal(location.get("id"), id));
        }
        if (slug != null) {
            specs.add((Specification<Location>) (location, query, cb) -> cb.equal(location.get("slug"), slug));
        }
        return specs
                .stream()
                .reduce(Specification::and)
                .orElse(null);
    }

}
