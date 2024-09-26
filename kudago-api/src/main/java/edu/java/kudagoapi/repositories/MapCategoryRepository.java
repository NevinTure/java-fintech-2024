package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Category;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MapCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> categories;

    public MapCategoryRepository() {
        this.categories = new ConcurrentHashMap<>();
    }

    @Override
    public Category save(Category category) {
        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public List<Category> saveAll(List<Category> categoryList) {
        for (Category category : categoryList) {
            categories.put(category.getId(), category);
        }
        return categoryList;
    }

    @Override
    public Optional<Category> findById(long id) {
        return Optional.of(categories.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public void deleteById(long id) {
        categories.remove(id);
    }
}
