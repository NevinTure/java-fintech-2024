package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.services.LocationService;
import edu.java.kudagoapi.services.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class LocationServiceInitializedEventApplicationListener
        implements ApplicationListener<LocationServiceInitializedEvent> {

    private final KudagoClient kudagoClient;
    private final static String FIELDS = "name,slug,language";
    private final ExecutorService kudagoUpdateExecutor;
    private final ScheduledExecutorService kudagoUpdateScheduler;
    @Value("${app.update-delay}")
    private Duration updateDelay;

    @Override
    @Timed
    public void onApplicationEvent(LocationServiceInitializedEvent event) {
        LocationService locationService = (LocationServiceImpl) event.getSource();
        kudagoUpdateScheduler.schedule(
                () -> kudagoUpdateExecutor
                        .submit(() -> locationService.saveAll(kudagoClient.getLocations(FIELDS))),
                updateDelay.getSeconds(), TimeUnit.SECONDS);
    }
}
