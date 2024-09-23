package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.LocationDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface LocationService {

    ResponseEntity<Object> save(LocationDto dto, String id);

    ResponseEntity<Object> saveAll(List<LocationDto> dtos);

    ResponseEntity<LocationDto> findById(String id);

    ResponseEntity<Object> deleteById(String id);
}
