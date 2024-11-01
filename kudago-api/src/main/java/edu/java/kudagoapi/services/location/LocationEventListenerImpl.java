package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import edu.java.kudagoapi.utils.LocationRequestOperation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationEventListenerImpl implements LocationEventListener {

    private final JpaLocationRepository repository;
    private final LocationEventManager eventManager;

    @PostConstruct
    public void init() {
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventManager.subscribe(LocationRequestOperation.SAVE, this);
    }

    @Override
    public void update(LocationRequestOperation op, Location location) {
        if (op == LocationRequestOperation.SAVE) {
            repository.save(location);
        }
    }
}
