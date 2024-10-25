package edu.java.kudagoapi;

import edu.java.kudagoapi.model.Event;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaEventRepository;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IntegrationTest extends IntegrationEnvironment {

    private final JpaEventRepository eventRepository;
    private final JpaLocationRepository locationRepository;

    @Autowired
    public IntegrationTest(JpaEventRepository eventRepository, JpaLocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }

    @Test
    public void test() {
        Location location = new Location(
            "test",
            "test",
                "ru"
        );
        locationRepository.save(location);
        Event event1 = new Event(
                "test1", "test1", LocalDate.of(2024, 10, 10),
                location
        );
        eventRepository.save(event1);
        Event event2 = new Event(
                "test2", "test2", LocalDate.of(2024, 5, 10),
                location
        );
        eventRepository.save(event2);
        List<Event> events = eventRepository.findAll(JpaEventRepository
                .buildSpecification("test1", null, null, null));
        System.out.println(events.getFirst().getLocation());
    }
}
