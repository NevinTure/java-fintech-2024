package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.dtos.events.*;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.services.event.EventCollectorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest extends IntegrationEnvironment {

    @MockBean
    private EventCollectorService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetEventWithFuture() throws Exception {
        //when
        List<EventDtoResponse> events = List.of(
                new EventDtoResponse(1L, new PlaceDto(), new LocationDto(), "200"),
                new EventDtoResponse(2L, new PlaceDto(), new LocationDto(), "300")
        );
        Mockito.when(service
                        .getEventsWithFuture(new BigDecimal(5), "USD", null, null))
                .thenReturn(CompletableFuture
                        .completedFuture(new EventsResponse(events)));

        //then
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/events/future")
                        .queryParam("budget", "5")
                        .queryParam("currency", "USD"))
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                  "results": [
                                                {
                                                "id": 1,
                                                "place": {},
                                                "location": {},
                                                "price": "200"
                                                },
                                                {
                                                  "id": 2,
                                                  "place": {},
                                                  "location": {},
                                                  "price": "300"
                                                }
                                             ]
                                }
                                """));
    }

    @Test
    public void testGetEventWithFutureWhenError() throws Exception {
        //when
        Mockito.when(service
                        .getEventsWithFuture(new BigDecimal(-5), "USD", null, null))
                .thenThrow(BadRequestApiException.class);

        //then
        mockMvc.perform(get("/api/v1/events/future")
                        .queryParam("budget", "-5")
                        .queryParam("currency", "USD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetEventWithReactor() throws Exception {
        //when
        List<EventDtoResponse> events = List.of(
                new EventDtoResponse(1L, new PlaceDto(), new LocationDto(), "200"),
                new EventDtoResponse(2L, new PlaceDto(), new LocationDto(), "300")
        );
        Mockito.when(service
                        .getEventsWithReactor(new BigDecimal(5), "USD", null, null))
                .thenReturn(Mono.just(new EventsResponse(events)));

        //then
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/events/reactor")
                        .queryParam("budget", "5")
                        .queryParam("currency", "USD"))
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                  "results": [
                                                {
                                                "id": 1,
                                                "place": {},
                                                "location": {},
                                                "price": "200"
                                                },
                                                {
                                                  "id": 2,
                                                  "place": {},
                                                  "location": {},
                                                  "price": "300"
                                                }
                                             ]
                                }
                                """));
    }

    @Test
    public void testGetEventWithReactorWhenError() throws Exception {
        //when
        Mockito.when(service
                        .getEventsWithReactor(new BigDecimal(-5), "USD", null, null))
                .thenThrow(BadRequestApiException.class);

        //then
        mockMvc.perform(get("/api/v1/events/reactor")
                        .queryParam("budget", "-5")
                        .queryParam("currency", "USD"))
                .andExpect(status().isBadRequest());
    }
}
