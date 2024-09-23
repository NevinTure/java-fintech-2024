package edu.java.kudagoapi.services;

import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.NotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;
    private final ModelMapper mapper;
    private final KudagoClient kudagoClient;
    private final static String FIELDS = "name,slug,language";

    public LocationServiceImpl(
            LocationRepository repository, ModelMapper mapper, KudagoClient kudagoClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.kudagoClient = kudagoClient;
    }

    @PostConstruct
    private void initialFill() {
        saveAll(kudagoClient.getLocations(FIELDS));
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
    public ResponseEntity<LocationDto> findById(String id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            return ResponseEntity
                    .ok(mapper.map(locationOptional.get(), LocationDto.class));
        }
        throw new NotFoundApiException(
                String.format("Location with id %s not found", id)
        );
    }

    @Override
    public ResponseEntity<Object> deleteById(String id) {
        Optional<Location> locationOptional = repository.findById(id);
        if (locationOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundApiException(
                String.format("Location with id %s not found", id)
        );
    }
}
