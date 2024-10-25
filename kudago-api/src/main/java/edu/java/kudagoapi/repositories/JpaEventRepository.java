package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Event;
import edu.java.kudagoapi.model.Location;
import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.*;

@Repository
public interface JpaEventRepository extends JpaRepository<Event, Long> {

    @Query("from Event e join fetch e.location where e.id = :id")
    Optional<Event> findById(@Param("id") @NotNull Long id);

    List<Event> findAll(Specification<Event> spec);

    static Specification<Event> buildSpecification(String slug, Location location, LocalDate fromDate, LocalDate toDate) {
        List<Specification<Event>> specs = new ArrayList<>();
        if (slug != null) {
            specs.add((Specification<Event>) (event, query, cb) -> cb.equal(event.get("slug"), slug));
        }
        if (location != null) {
            specs.add((Specification<Event>) (event, query, cb) -> cb.equal(event.get("location"), location));
        }
        if (fromDate != null) {
            specs.add((Specification<Event>) (event, query, cb) -> cb.greaterThanOrEqualTo(event.get("date"), fromDate));
        }
        if (toDate != null) {
            specs.add((Specification<Event>) (event, query, cb) -> cb.lessThanOrEqualTo(event.get("date"), toDate));
        }
        return specs
                .stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}
