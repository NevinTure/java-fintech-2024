package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.utils.CategoryRequestOperation;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CategoryEventManager {

    private final Map<CategoryRequestOperation, List<CategoryEventListener>> listeners;

    public CategoryEventManager() {
        listeners = new ConcurrentHashMap<>();
    }

    public void subscribe(CategoryRequestOperation op, CategoryEventListener listener) {
        listeners.computeIfAbsent(op, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(CategoryRequestOperation op, CategoryEventListener listener) {
        listeners.get(op).remove(listener);
    }

    public void notify(CategoryRequestOperation op, Category category) {
        for (CategoryEventListener listener : listeners.get(op)) {
            listener.update(op, category);
        }
    }
}
