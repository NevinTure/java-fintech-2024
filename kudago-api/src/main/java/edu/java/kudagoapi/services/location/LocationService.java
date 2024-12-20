package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.dtos.LocationDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface LocationService {

    ResponseEntity<Object> save(LocationDto dto);

    ResponseEntity<Object> saveAll(List<LocationDto> dtos);

    ResponseEntity<LocationDto> getById(Long id);

    ResponseEntity<List<LocationDto>> findAll();

    ResponseEntity<Object> deleteById(Long id);
}
