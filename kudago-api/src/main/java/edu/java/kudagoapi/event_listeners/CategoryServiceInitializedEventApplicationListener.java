package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.services.CategoryService;
import edu.java.kudagoapi.services.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceInitializedEventApplicationListener
        implements ApplicationListener<CategoryServiceInitializedEvent> {

    private final KudagoClient kudagoClient;
    private final ThreadPoolTaskExecutor kudagoUpdateExecutor;
    private final ScheduledExecutorService kudagoUpdateScheduler;
    @Value("${app.update-delay}")
    private Duration updateDelay;

    @Override
    @Timed
    public void onApplicationEvent(CategoryServiceInitializedEvent event) {
        CategoryService service = (CategoryServiceImpl) event.getSource();
        kudagoUpdateScheduler.schedule(
                () -> kudagoUpdateExecutor
                        .submit(() -> service.saveAll(kudagoClient.getCategories())),
                updateDelay.getSeconds(), TimeUnit.SECONDS);
        service.saveAll(kudagoClient.getCategories());
    }
}
