package edu.java.kudagoapi.controllers;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.services.location.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<LocationDto> getById(@PathVariable("id") @Min(1) long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid LocationDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putUpdate(
            @PathVariable("id") @Min(1) long id, @RequestBody @Valid LocationDto dto) {
        return service.fullUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") @Min(1) long id) {
        return service.deleteById(id);
    }
}
