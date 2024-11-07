package edu.java.kudagoapi.commands;

import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.services.category.CategoryService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class InitializeCategoryCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final CategoryService categoryService;

    @Lazy
    public InitializeCategoryCommand(ApplicationEventPublisher eventPublisher, CategoryService categoryService) {
        this.eventPublisher = eventPublisher;
        this.categoryService = categoryService;
    }

    @Override
    public boolean execute() {
        try {
            eventPublisher.publishEvent(new CategoryServiceInitializedEvent(categoryService));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
