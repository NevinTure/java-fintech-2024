//package edu.java.kudagoapi;
//
//import edu.java.kudagoapi.dtos.LocationDto;
//import edu.java.kudagoapi.dtos.events.EventDto;
//import edu.java.kudagoapi.dtos.events.EventDtoResponse;
//import edu.java.kudagoapi.model.Event;
//import edu.java.kudagoapi.model.Location;
//import edu.java.kudagoapi.repositories.JpaEventRepository;
//import edu.java.kudagoapi.repositories.JpaLocationRepository;
//import edu.java.kudagoapi.services.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class IntegrationTest extends IntegrationEnvironment {
//
//    private final LocationService locationService;
//    private final EventService eventService;
//    private final JpaLocationRepository locationRepository;
//    private final JpaEventRepository eventRepository;
//
//    @Autowired
//    public IntegrationTest(JpaLocationService locationService, EventService eventService, JpaLocationRepository locationRepository, JpaEventRepository eventRepository) {
//        this.locationService = locationService;
//        this.eventService = eventService;
//        this.locationRepository = locationRepository;
//        this.eventRepository = eventRepository;
//    }
//
//
//    @Test
//    public void testContainer() {
//        assertThat(POSTGRES.isCreated()).isTrue();
//        assertThat(POSTGRES.isRunning()).isTrue();
//    }
//
//    @Test
//    public void test() {
//        Location location1 = new Location(
//            "test1",
//            "test1",
//                "ru"
//        );
//        locationRepository.save(location1);
//        Location location2 = new Location(
//                "test2",
//                "test2",
//                "ru"
//        );
//        locationRepository.save(location2);
//        Event event1 = new Event(
//                "test1", "test1", LocalDate.of(2024, 10, 10),
//                location1
//        );
//        eventRepository.save(event1);
//        Event event2 = new Event(
//                "test1", "test2", LocalDate.of(2024, 5, 10),
//                location2
//        );
//        eventRepository.save(event2);
//        List<Event> events = eventRepository.findAll(JpaEventRepository
//                .buildSpecification("test1", null, null, null));
//        events.forEach(System.out::println);
//    }
//}
