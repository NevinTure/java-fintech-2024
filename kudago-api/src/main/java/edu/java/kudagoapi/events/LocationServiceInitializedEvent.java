package edu.java.kudagoapi.events;

import org.springframework.context.ApplicationEvent;

public class LocationServiceInitializedEvent extends ApplicationEvent {
    public LocationServiceInitializedEvent(Object source) {
        super(source);
    }
}
