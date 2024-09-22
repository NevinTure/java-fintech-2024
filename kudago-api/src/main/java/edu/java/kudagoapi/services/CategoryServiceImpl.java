package edu.java.kudagoapi.services;

import edu.java.kudagoapi.exceptions.NotFoundApiException;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Object> save(Category category) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> saveAll(List<Category> categories) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Category> getById(long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            return ResponseEntity.ok(categoryOptional.get());
        }
        throw new NotFoundApiException(
                String.format("Category with id %d not found", id));
    }

    @Override
    public ResponseEntity<Object> deleteById(long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundApiException(
                String.format("Category with id %d not found", id));
    }
}
