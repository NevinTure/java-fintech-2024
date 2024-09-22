package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MapLocationRepository implements LocationRepository {

    private final Map<Long, Location> locations;
    private final AtomicLong sequence;

    public MapLocationRepository() {
        this.locations = new ConcurrentHashMap<>();
        this.sequence = new AtomicLong(1);
    }

    @Override
    public Location save(Location location) {
        long id = sequence.getAndIncrement();
        location.setId(id);
        locations.put(id, location);
        return location;
    }

    @Override
    public List<Location> saveAll(List<Location> locationList) {
        for (Location location : locationList) {
            long id = sequence.getAndIncrement();
            location.setId(id);
            locations.put(id, location);
        }
        return locationList;
    }

    @Override
    public Optional<Location> findById(long id) {
        return Optional.of(locations.get(id));
    }

    @Override
    public void deleteById(long id) {
        locations.remove(id);
    }
}
