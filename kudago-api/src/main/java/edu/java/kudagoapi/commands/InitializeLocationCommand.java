package edu.java.kudagoapi.commands;

import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.services.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeLocationCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocationService locationService;

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
