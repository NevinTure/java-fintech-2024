package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.utils.LocationRequestOperation;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocationEventManager {

    private final Map<LocationRequestOperation, List<LocationEventListener>> listeners;

    public LocationEventManager() {
        listeners = new ConcurrentHashMap<>();
    }

    public void subscribe(LocationRequestOperation op, LocationEventListener listener) {
        listeners.computeIfAbsent(op, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(LocationRequestOperation op, LocationEventListener listener) {
        listeners.get(op).remove(listener);
    }

    public void notify(LocationRequestOperation op, Location location) {
        for (LocationEventListener listener : listeners.get(op)) {
            listener.update(op, location);
        }
    }
}
