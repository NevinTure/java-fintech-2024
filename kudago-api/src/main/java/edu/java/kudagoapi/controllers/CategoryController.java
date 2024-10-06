package edu.java.kudagoapi.controllers;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places/categories")
@RequiredArgsConstructor
@Timed
@Validated
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable("id") @Min(0) long id) {
        return service.getById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> create(
            @PathVariable("id") @Min(0) long id, @RequestBody @Valid CategoryDto dto) {
        return service.save(dto, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putUpdate(
            @PathVariable("id") @Min(0) long id, @RequestBody @Valid CategoryDto dto) {
        return service.fullUpdate(dto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") @Min(0) long id) {
        return service.deleteById(id);
    }
}
