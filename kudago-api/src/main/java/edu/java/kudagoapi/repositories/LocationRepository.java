package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    Optional<Location> findById(long id);

    void deleteById(long id);
}
