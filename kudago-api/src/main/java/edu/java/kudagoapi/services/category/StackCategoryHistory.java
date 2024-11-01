package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.exceptions.CategoryNotFoundApiException;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.model.CategorySnapshot;
import edu.java.kudagoapi.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StackCategoryHistory implements CategoryHistory {

    private final Map<Long, Deque<CategorySnapshot>> history;
    private final CategoryRepository repo;
    private final ModelMapper mapper;

    public StackCategoryHistory(CategoryRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
        history = new ConcurrentHashMap<>();
    }

    @Override
    public void push(Long id, Category category) {
        CategorySnapshot snapshot = mapper.map(category, CategorySnapshot.class);
        history.computeIfAbsent(id, k -> new LinkedList<>()).addLast(snapshot);
    }

    @Override
    public Category poll(Long id) {
        if (history.containsKey(id)) {
            CategorySnapshot snapshot = history.get(id).pollLast();
            Category category = mapper.map(snapshot, Category.class);
            repo.save(category);
            return category;
        }
        throw new CategoryNotFoundApiException(id);
    }
}
