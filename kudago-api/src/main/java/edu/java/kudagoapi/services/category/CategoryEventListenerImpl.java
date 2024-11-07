package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import edu.java.kudagoapi.utils.CategoryRequestOperation;
import org.springframework.stereotype.Service;

@Service
public class CategoryEventListenerImpl implements CategoryEventListener {

    private final CategoryRepository repository;
    private final CategoryEventManager eventManager;

    public CategoryEventListenerImpl(CategoryRepository repository, CategoryEventManager eventManager) {
        this.repository = repository;
        this.eventManager = eventManager;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventManager.subscribe(CategoryRequestOperation.SAVE, this);
    }

    @Override
    public void update(CategoryRequestOperation op, Category category) {
        if (op == CategoryRequestOperation.SAVE) {
            repository.save(category);
        }
    }
}
