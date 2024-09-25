package edu.java.kudagoapi.controllers;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.services.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Timed
@Validated
public class LocationController {

    private final LocationService service;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getById(@PathVariable("id") @Size(min = 1) String id) {
        return service.getById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> create(
            @PathVariable("id") @Size(min = 1) String id, @RequestBody @Valid LocationDto dto) {
        return service.save(dto, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putUpdate(
            @PathVariable("id") @Size(min = 1) String id, @RequestBody @Valid LocationDto dto) {
        return service.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") @Size(min = 1) String id) {
        return service.deleteById(id);
    }
}
