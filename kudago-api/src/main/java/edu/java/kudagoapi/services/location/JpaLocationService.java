package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import edu.java.kudagoapi.utils.LocationRequestOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", value = "database-access-type", havingValue = "jpa")
@Service
public class JpaLocationService implements UpdatableLocationService {

    private final JpaLocationRepository repo;
    private final ModelMapper mapper;
    private final LocationEventManager eventManager;
    private final LocationHistory history;

    @Override
    public ResponseEntity<Object> save(LocationDto dto) {
        Location location = mapper.map(dto, Location.class);
        eventManager.notify(LocationRequestOperation.SAVE, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> saveAll(List<LocationDto> dtos) {
        repo.saveAll(dtos
                .stream()
                .map(v -> mapper.map(v, Location.class))
                .toList());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LocationDto> getById(Long id) {
        Optional<Location> locationOp = repo.findById(id);
        if (locationOp.isPresent()) {
            LocationDto dto = mapper.map(locationOp.get(), LocationDto.class);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        throw new LocationNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<List<LocationDto>> findAll() {
        return new ResponseEntity<>(repo.findAll()
                .stream()
                .map(v -> mapper.map(v, LocationDto.class))
                .toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fullUpdate(Long id, LocationDto dto) {
        Optional<Location> locationOp = repo.findById(id);
        if (locationOp.isPresent()) {
            history.push(id, locationOp.get());
            dto.setId(id);
            return save(dto);
        }
        throw new LocationNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> undoUpdate(Long id) {
        history.poll(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Optional<Location> locationOp = repo.findById(id);
        if (locationOp.isPresent()) {
            repo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new LocationNotFoundApiException(id);
    }
}
