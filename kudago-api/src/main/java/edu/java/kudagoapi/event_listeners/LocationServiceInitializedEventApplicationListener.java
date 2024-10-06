package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.services.LocationService;
import edu.java.kudagoapi.services.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceInitializedEventApplicationListener
        implements ApplicationListener<LocationServiceInitializedEvent> {

    private final KudagoClient kudagoClient;
    private final static String FIELDS = "name,slug,language";

    @Override
    @Timed
    public void onApplicationEvent(LocationServiceInitializedEvent event) {
        LocationService locationService = (LocationServiceImpl) event.getSource();
        locationService.saveAll(kudagoClient.getLocations(FIELDS));
    }
}
