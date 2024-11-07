package edu.java.kudagoapi.services;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.dtos.events.*;
import edu.java.kudagoapi.exceptions.ApiException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class EventServiceTest extends IntegrationEnvironment {

    @Autowired
    private EventCollectorService service;

    @Test
    @SneakyThrows
    public void testGetEventsWithFuture() {
        //when
        EventsResponse response = service.getEventsWithFuture(
                new BigDecimal(100),
                "RUB",
                LocalDate.of(2024, 8, 12),
                LocalDate.of(2024, 10, 12)).get();

        //then
        EventDtoResponse expectedFirst = new EventDtoResponse(
                192427L,
                null,
                new LocationDto(
                        "Saint Petersburg",
                        "spb",
                        "ru"
                ),
                ""
        );
        assertThat(response.getEvents().size()).isEqualTo(6);
        assertThat(response.getEvents().getFirst()).isEqualTo(expectedFirst);
    }

    @Test
    @SneakyThrows
    public void testGetEventsWithFutureWhenError() {
        assertThat(service
                .getEventsWithFuture(new BigDecimal(1L), "TEST", null, null))
                .failsWithin(10, TimeUnit.SECONDS);
    }

    @Test
    @SneakyThrows
    public void testGetEventsWithReactor() {
        //when
        EventsResponse response = service.getEventsWithReactor(
                new BigDecimal(100),
                "RUB",
                LocalDate.of(2024, 8, 12),
                LocalDate.of(2024, 10, 12)).block();

        //then
        EventDtoResponse expectedFirst = new EventDtoResponse(
                192427L,
                null,
                new LocationDto(
                        "Saint Petersburg",
                        "spb",
                        "ru"
                ),
                ""
        );
        assertThat(response.getEvents().size()).isEqualTo(6);
        assertThat(response.getEvents().getFirst()).isEqualTo(expectedFirst);
    }

    @Test
    @SneakyThrows
    public void testGetEventsWithReactorWhenError() {
        assertThatExceptionOfType(ApiException.class).isThrownBy(
                () -> service
                        .getEventsWithReactor(new BigDecimal(1L), "TEST", null, null).block());
    }
}
