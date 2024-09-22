package edu.java.kudagoapi.services;

import edu.java.kudagoapi.exceptions.NotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;

    public LocationServiceImpl(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Object> save(Location location) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> saveAll(List<Location> locations) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Location> findById(Long id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            return ResponseEntity.ok(locationOptional.get());
        }
        throw new NotFoundApiException(
                String.format("Location with id %d not found", id)
        );
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundApiException(
                String.format("Location with id %d not found", id)
        );
    }
}
