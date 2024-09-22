package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Category;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MapCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> categories;
    private final AtomicLong sequence;

    public MapCategoryRepository() {
        this.categories = new ConcurrentHashMap<>();
        this.sequence = new AtomicLong(1);
    }

    @Override
    public Category save(Category category) {
        long id = sequence.getAndIncrement();
        category.setId(id);
        categories.put(id, category);
        return category;
    }

    @Override
    public List<Category> saveAll(List<Category> categoryList) {
        for (Category category : categoryList) {
            long id = sequence.getAndIncrement();
            category.setId(id);
            categories.put(id, category);
        }
        return categoryList;
    }

    @Override
    public Optional<Category> findById(long id) {
        return Optional.of(categories.get(id));
    }

    @Override
    public void deleteById(long id) {
        categories.remove(id);
    }
}
