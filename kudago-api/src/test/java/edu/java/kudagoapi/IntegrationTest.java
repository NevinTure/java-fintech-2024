package edu.java.kudagoapi;

import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import edu.java.kudagoapi.services.location.JpaLocationHistory;
import edu.java.kudagoapi.services.location.JpaLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest extends IntegrationEnvironment {

    @Autowired
    private JpaLocationRepository repo;
    @Autowired
    private JpaLocationHistory history;
    @Autowired
    private JpaLocationService locationService;

    @Test
    public void test() {
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "test"
        );
        locationService.save(dto);
        LocationDto dto2 = new LocationDto(
                "test1",
                "test1",
                "test1"
        );
        locationService.fullUpdate(1L, dto2);
        Location first = repo.findAll().getFirst();
        System.out.println(first);
        Location poll = history.poll(first.getId());
        System.out.println(poll);
        first = repo.findAll().getFirst();
        System.out.println(first);

    }
}
