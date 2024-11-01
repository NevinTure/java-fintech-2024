package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.commands.Command;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", value = "database-access-type", havingValue = "map")
@Service
public class MapLocationService implements LocationService {

    private final LocationRepository repository;
    private final ModelMapper mapper;
    private final Command initializeLocationCommand;

    @PostConstruct
    public void init() {
        initializeLocationCommand.execute();
    }

    @Override
    public ResponseEntity<Object> save(LocationDto dto) {
        if (repository.findBySlug(dto.getSlug()).isPresent()) {
            throw new BadRequestApiException(
                    String.format("Key (slug)=(%s) already exists.", dto.getSlug()));
        }
        Location location = mapper.map(dto, Location.class);
        repository.save(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> saveAll(List<LocationDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        List<Location> locations = dtos
                .stream()
                .map(v -> mapper.map(v, Location.class))
                .toList();
        repository.saveAll(locations);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LocationDto> getById(Long id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            return ResponseEntity
                    .ok(mapper.map(locationOptional.get(), LocationDto.class));
        }
        throw new LocationNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<List<LocationDto>> findAll() {
        return ResponseEntity.ok(repository
                .findAll()
                .stream()
                .map(v -> mapper.map(v, LocationDto.class))
                .toList());
    }

    @Override
    public ResponseEntity<Object> fullUpdate(Long id, LocationDto dto) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            Location location = mapper.map(dto, Location.class);
            location.setId(id);
            repository.save(location);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new LocationNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new LocationNotFoundApiException(id);
    }
}
