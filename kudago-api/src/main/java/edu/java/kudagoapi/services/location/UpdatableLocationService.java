package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.dtos.LocationDto;
import org.springframework.http.ResponseEntity;

public interface UpdatableLocationService extends LocationService {

    ResponseEntity<Object> fullUpdate(Long id, LocationDto dto);

    ResponseEntity<Object> undoUpdate(Long id);
}
