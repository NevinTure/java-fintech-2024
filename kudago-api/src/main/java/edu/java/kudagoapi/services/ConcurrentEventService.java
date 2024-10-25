package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.events.EventsResponse;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface ConcurrentEventService extends EventService {

    CompletableFuture<EventsResponse> getEventsWithFuture(
            BigDecimal budget, String currency, LocalDate from, LocalDate to);

    Mono<EventsResponse> getEventsWithReactor(
            BigDecimal budget, String currency, LocalDate from, LocalDate to);
}
