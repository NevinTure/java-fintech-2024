package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    List<Location> saveAll(List<Location> locationList);

    Optional<Location> findById(String id);

    void deleteById(String id);
}
