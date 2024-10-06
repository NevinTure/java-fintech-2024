package edu.java.kudagoapi.events;

import org.springframework.context.ApplicationEvent;

public class CategoryServiceInitializedEvent extends ApplicationEvent {
    public CategoryServiceInitializedEvent(Object source) {
        super(source);
    }
}
