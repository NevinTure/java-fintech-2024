package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.CategoryDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CategoryService {

    ResponseEntity<Object> save(CategoryDto dto, long id);

    ResponseEntity<Object> saveAll(List<CategoryDto> dtos);

    ResponseEntity<CategoryDto> getById(long id);

    ResponseEntity<List<CategoryDto>> findAll();

    ResponseEntity<Object> fullUpdate(CategoryDto dto, long id);

    ResponseEntity<Object> deleteById(long id);
}
