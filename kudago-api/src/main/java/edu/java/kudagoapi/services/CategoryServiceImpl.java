package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.CategoryNotFoundApiException;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        eventPublisher.publishEvent(new CategoryServiceInitializedEvent(this));
    }

    @Override
    public ResponseEntity<Object> save(CategoryDto dto, long id) {
        validateParams(id);
        Category category = mapper.map(dto, Category.class);
        category.setId(id);
        repository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateParams(long id) {
        if (repository.findById(id).isPresent()) {
            throw new BadRequestApiException(
                    String.format("Category with id %d already exists", id));
        }
    }

    @Override
    public ResponseEntity<Object> saveAll(List<CategoryDto> dtos) {
        List<Category> categories = dtos
                .stream()
                .map(v -> mapper.map(v, Category.class))
                .toList();
        repository.saveAll(categories);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDto> getById(long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            return ResponseEntity
                    .ok(mapper.map(categoryOptional.get(), CategoryDto.class));
        }
        throw new CategoryNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(repository
                .findAll()
                .stream()
                .map(v -> mapper.map(v, CategoryDto.class))
                .toList());
    }

    @Override
    public ResponseEntity<Object> fullUpdate(CategoryDto dto, long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = mapper.map(dto, Category.class);
            category.setId(id);
            repository.save(category);
        }
        throw new CategoryNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> deleteById(long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new CategoryNotFoundApiException(id);
    }
}
