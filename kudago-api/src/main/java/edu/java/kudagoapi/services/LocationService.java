package edu.java.kudagoapi.services;

import edu.java.kudagoapi.model.Location;
import org.springframework.http.ResponseEntity;
import java.util.*;

public interface LocationService {

    ResponseEntity<Object> save(Location location);

    ResponseEntity<Object> saveAll(List<Location> locations);

    ResponseEntity<Location> findById(Long id);

    ResponseEntity<Object> deleteById(Long id);
}
