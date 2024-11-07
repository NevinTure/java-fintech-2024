package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.dtos.EventFilter;
import edu.java.kudagoapi.dtos.events.EventDto;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.services.event.EventCollectorService;
import edu.java.kudagoapi.services.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventCollectorService collectorService;
    private final EventService eventService;

    @GetMapping("/future")
    public CompletableFuture<EventsResponse> getEventsWithFuture(
            @RequestParam("budget") BigDecimal budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) LocalDate from,
            @RequestParam(value = "dateTo", required = false) LocalDate to) {
        return collectorService.getEventsWithFuture(budget, currency, from, to);
    }

    @GetMapping("/reactor")
    public Mono<EventsResponse> getEventsWithReactor(
            @RequestParam("budget") BigDecimal budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) LocalDate from,
            @RequestParam(value = "dateTo", required = false) LocalDate to) {
        return collectorService.getEventsWithReactor(budget, currency, from, to);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getById(@PathVariable("id") @Min(1) Long id) {
        return eventService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventDto>> getAllByFilter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String locationSlug,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {
        return eventService
                .findAllByFilter(new EventFilter(title, locationSlug, fromDate, toDate));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid EventDto eventDto) {
        return eventService.save(eventDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> fullUpdate(
            @PathVariable("id") @Min(1) Long id,
            @Valid @RequestBody EventDto eventDto) {
        return eventService.fullUpdate(id, eventDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") @Min(1) Long id) {
        return eventService.deleteById(id);
    }
}
