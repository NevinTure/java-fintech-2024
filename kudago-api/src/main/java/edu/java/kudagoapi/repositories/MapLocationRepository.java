package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MapLocationRepository implements LocationRepository {

    private final Map<String, Location> locations;

    public MapLocationRepository() {
        this.locations = new ConcurrentHashMap<>();
    }

    @Override
    public Location save(Location location) {
        locations.put(location.getName(), location);
        return location;
    }

    @Override
    public List<Location> saveAll(List<Location> locationList) {
        if (locationList == null || locationList.isEmpty()) {
            return List.of();
        }
        for (Location location : locationList) {
            locations.put(location.getName(), location);
        }
        return locationList;
    }

    @Override
    public Optional<Location> findById(String id) {
        return Optional.of(locations.get(id));
    }

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations.values());
    }

    @Override
    public void deleteById(String id) {
        locations.remove(id);
    }
}
