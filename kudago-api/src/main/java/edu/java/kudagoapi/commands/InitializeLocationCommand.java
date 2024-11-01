package edu.java.kudagoapi.commands;

import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.services.location.LocationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class InitializeLocationCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocationService locationService;

    @Lazy
    public InitializeLocationCommand(ApplicationEventPublisher eventPublisher, LocationService locationService) {
        this.eventPublisher = eventPublisher;
        this.locationService = locationService;
    }

    @Override
    public boolean execute() {
        try {
            eventPublisher.publishEvent(new LocationServiceInitializedEvent(locationService));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
