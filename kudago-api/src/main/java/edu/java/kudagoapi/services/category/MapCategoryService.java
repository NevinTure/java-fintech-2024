package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.commands.Command;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.CategoryNotFoundApiException;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import edu.java.kudagoapi.utils.CategoryRequestOperation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapCategoryService implements UpdatableCategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;
    private final Command initializeCategoryCommand;
    private final CategoryEventManager eventManager;
    private final CategoryHistory history;

    @PostConstruct
    public void init() {
        initializeCategoryCommand.execute();
    }

    @Override
    public ResponseEntity<Object> save(CategoryDto dto, long id) {
        validateParams(id);
        Category category = mapper.map(dto, Category.class);
        category.setId(id);
        eventManager.notify(CategoryRequestOperation.SAVE, category);
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
        if (dtos == null || dtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        validateDtos(dtos);
        List<Category> categories = dtos
                .stream()
                .map(v -> mapper.map(v, Category.class))
                .toList();
        repository.saveAll(categories);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateDtos(List<CategoryDto> dtos) {
        for (CategoryDto dto : dtos) {
            if (dto.getId() == null) {
                throw new BadRequestApiException("When saveAll category ids must be specified");
            }
        }
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
            history.push(id, categoryOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
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

    @Override
    public ResponseEntity<Object> undoUpdate(long id) {
        history.poll(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
