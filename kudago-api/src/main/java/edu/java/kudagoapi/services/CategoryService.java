package edu.java.kudagoapi.services;

import edu.java.kudagoapi.model.Category;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    ResponseEntity<Object> save(Category category);

    ResponseEntity<Object> saveAll(List<Category> categories);

    ResponseEntity<Category> getById(long id);

    ResponseEntity<Object> deleteById(long id);
}
