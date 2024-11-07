package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.services.category.CategoryService;
import edu.java.kudagoapi.services.category.MapCategoryService;
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
public class CategoryServiceInitializedEventApplicationListener
        implements ApplicationListener<CategoryServiceInitializedEvent> {

    private final KudagoClient kudagoClient;
    private final ThreadPoolTaskExecutor kudagoExecutor;
    private final ScheduledExecutorService kudagoUpdateScheduler;
    @Value("${app.update-delay}")
    private Duration updateDelay;

    @Override
    @Timed
    public void onApplicationEvent(CategoryServiceInitializedEvent event) {
        CategoryService service = (MapCategoryService) event.getSource();
        kudagoUpdateScheduler.schedule(
                () -> kudagoExecutor
                        .submit(() -> service.saveAll(kudagoClient.getCategories())),
                updateDelay.getSeconds(), TimeUnit.SECONDS);
        service.saveAll(kudagoClient.getCategories());
    }
}
