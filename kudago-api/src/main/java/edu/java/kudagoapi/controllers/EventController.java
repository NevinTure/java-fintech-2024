package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @GetMapping("/future")
    public CompletableFuture<EventsResponse> getEventsWithFuture(
            @RequestParam("budget") BigDecimal budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) LocalDate from,
            @RequestParam(value = "dateTo", required = false) LocalDate to) {
        return service.getEventsWithFuture(budget, currency, from, to);
    }

    @GetMapping("/reactor")
    public Mono<EventsResponse> getEventsWithReactor(
            @RequestParam("budget") BigDecimal budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) LocalDate from,
            @RequestParam(value = "dateTo", required = false) LocalDate to) {
        return service.getEventsWithReactor(budget, currency, from, to);
    }

}
