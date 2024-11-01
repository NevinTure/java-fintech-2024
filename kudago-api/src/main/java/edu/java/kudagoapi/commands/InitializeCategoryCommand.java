package edu.java.kudagoapi.commands;

import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeCategoryCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final CategoryService categoryService;

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
