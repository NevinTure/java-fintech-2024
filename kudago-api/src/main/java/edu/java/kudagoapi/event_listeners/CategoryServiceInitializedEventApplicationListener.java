package edu.java.kudagoapi.event_listeners;

import edu.java.customaspect.annotations.Timed;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.events.CategoryServiceInitializedEvent;
import edu.java.kudagoapi.services.CategoryService;
import edu.java.kudagoapi.services.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceInitializedEventApplicationListener
        implements ApplicationListener<CategoryServiceInitializedEvent> {

    private final KudagoClient kudagoClient;

    @Override
    @Timed
    public void onApplicationEvent(CategoryServiceInitializedEvent event) {
        CategoryService service = (CategoryServiceImpl) event.getSource();
        service.saveAll(kudagoClient.getCategories());
    }
}
