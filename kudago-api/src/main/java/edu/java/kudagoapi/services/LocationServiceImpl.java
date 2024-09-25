package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        eventPublisher.publishEvent(new LocationServiceInitializedEvent(this));
    }

    @Override
    public ResponseEntity<Object> save(LocationDto dto, String id) {
        validateParams(dto, id);
        Location location = mapper.map(dto, Location.class);
        repository.save(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateParams(LocationDto dto, String id) {
        if (repository.findById(id).isPresent()) {
            throw new BadRequestApiException(
                    String.format("Location with id: %s already exists", id));
        }
        if (!Objects.equals(dto.getName(), id)) {
            throw new BadRequestApiException("Id must match Location name");
        }
    }

    @Override
    public ResponseEntity<Object> saveAll(List<LocationDto> dtos) {
        List<Location> locations = dtos
                .stream()
                .map(v -> mapper.map(v, Location.class))
                .toList();
        repository.saveAll(locations);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LocationDto> getById(String id) {
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
    public ResponseEntity<Object> fullUpdate(LocationDto dto, String id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            Location location = mapper.map(dto, Location.class);
            location.setName(id);
            repository.save(location);
        }
        throw new LocationNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> deleteById(String id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new LocationNotFoundApiException(id);
    }
}
