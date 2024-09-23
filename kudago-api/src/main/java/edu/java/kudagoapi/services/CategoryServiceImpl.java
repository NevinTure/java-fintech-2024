package edu.java.kudagoapi.services;

import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.NotFoundApiException;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;
    private final KudagoClient kudagoClient;

    public CategoryServiceImpl(
            CategoryRepository repository, ModelMapper mapper, KudagoClient kudagoClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.kudagoClient = kudagoClient;
    }

    @PostConstruct
    private void initialFill() {
        saveAll(kudagoClient.getCategories());
    }

    @Override
    public ResponseEntity<Object> save(CategoryDto dto, long id) {
        validateParams(dto, id);
        Category category = mapper.map(dto, Category.class);
        category.setId(id);
        repository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateParams(CategoryDto dto, long id) {
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
