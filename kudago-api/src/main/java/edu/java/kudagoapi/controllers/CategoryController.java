package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/places/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long id) {
        return service.getById(id);
    }
}
