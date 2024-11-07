package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.LocationServiceInitializedEvent;
import edu.java.kudagoapi.services.LocationService;
import edu.java.kudagoapi.services.MapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "enable-update", havingValue = "true")
public class LocationServiceInitializedEventApplicationListener
        implements ApplicationListener<LocationServiceInitializedEvent> {

    private final KudagoClient kudagoClient;
    private final static String FIELDS = "name,slug,language";
    private final ThreadPoolTaskExecutor kudagoExecutor;
    private final ScheduledExecutorService kudagoUpdateScheduler;
    @Value("${app.update-delay}")
    private Duration updateDelay;

    @Override
    @Timed
    public void onApplicationEvent(LocationServiceInitializedEvent event) {
        LocationService locationService = (MapLocationService) event.getSource();
        kudagoUpdateScheduler.schedule(
                () -> kudagoExecutor
                        .submit(() -> locationService.saveAll(kudagoClient.getLocations(FIELDS))),
                updateDelay.getSeconds(), TimeUnit.SECONDS);
    }
}
