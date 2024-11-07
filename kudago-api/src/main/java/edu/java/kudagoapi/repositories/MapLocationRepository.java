package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Location;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MapLocationRepository implements LocationRepository {

    private final Map<Long, Location> locations;
    private final AtomicLong sequence;

    public MapLocationRepository() {
        this.locations = new ConcurrentHashMap<>();
        this.sequence = new AtomicLong(1);
    }

    @Override
    public Location save(Location location) {
        if (location.getId() == null) {
            long id = sequence.getAndIncrement();
            locations.put(id, location);
            location.setId(id);
        } else {
            locations.put(location.getId(), location);
        }
        return location;
    }

    @Override
    public List<Location> saveAll(List<Location> locationList) {
        for (Location location : locationList) {
            locations.put(sequence.getAndIncrement(), location);
        }
        return locationList;
    }

    @Override
    public Optional<Location> findById(Long id) {
        return Optional.of(locations.get(id));
    }

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations.values());
    }

    @Override
    public Optional<Location> findBySlug(String slug) {
        return locations
                .values()
                .stream()
                .filter(l -> l.getSlug().equals(slug))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        locations.remove(id);
    }
}
