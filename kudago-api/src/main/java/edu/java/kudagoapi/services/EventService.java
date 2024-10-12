package edu.java.kudagoapi.services;

import edu.java.kudagoapi.clients.CurrencyClient;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.CurrencyConvertRequest;
import edu.java.kudagoapi.dtos.CurrencyConvertResponse;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.utils.PriceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventService {

    private final CurrencyClient currencyClient;
    private final KudagoClient kudagoClient;
    private static final String RUBLE = "RUB";

    @Async
    public CompletableFuture<EventsResponse> getEventsWithFuture(
            BigDecimal budget, String currency, LocalDate from, LocalDate to) {

        CurrencyConvertRequest convertRequest = createConvertRequestWithDefaults(budget, currency);
        Map<String, String> eventsRequest = createEventsRequestWithDefaults(from, to);

        CompletableFuture<CurrencyConvertResponse> currencyResponse =
                CompletableFuture.supplyAsync(
                        () -> currencyClient.convert(convertRequest));
        CompletableFuture<EventsResponse> eventsResponse =
                CompletableFuture.supplyAsync(
                        () -> kudagoClient.getEvents(eventsRequest)
                );
        eventsResponse
                .thenAcceptBoth(currencyResponse, this::filterEventsResponse);
        return eventsResponse;
    }

    public Mono<EventsResponse> getEventsWithReactor(
            BigDecimal budget, String currency, LocalDate from, LocalDate to) {

        CurrencyConvertRequest convertRequest = createConvertRequestWithDefaults(budget, currency);
        Map<String, String> eventsRequest = createEventsRequestWithDefaults(from, to);

        Mono<CurrencyConvertResponse> curConvMono = Mono
                .fromSupplier(() -> currencyClient.convert(convertRequest));
        Mono<EventsResponse> eventsMono = Mono
                .fromSupplier(() -> kudagoClient.getEvents(eventsRequest));
        return eventsMono.zipWith(curConvMono, (evRes, curRes) -> {
            filterEventsResponse(evRes, curRes);
            return evRes;
        });
    }

    private CurrencyConvertRequest createConvertRequestWithDefaults(
            BigDecimal budget, String currency
    ) {
        return new CurrencyConvertRequest(
                currency,
                RUBLE,
                budget
        );
    }

    private Map<String, String> createEventsRequestWithDefaults(LocalDate from, LocalDate to) {
        Map<String, String> request = new HashMap<>();
        request.put("actual_since", (from == null ? LocalDate.MIN : from).toString());
        request.put("actual_until", (to == null ? LocalDate.MAX : to).toString());
        request.put("fields", "id,place,location,price");
        request.put("expand", "place,location");
        request.put("page_size", "100");
        return request;
    }

    private void filterEventsResponse(EventsResponse evRes, CurrencyConvertResponse curRes) {
        evRes.setEvents(evRes
                .getEvents()
                .stream()
                .filter(v -> PriceParser
                        .toBigDecimal(v.getPrice()).compareTo(curRes.getConvertedAmount()) <= 0)
                .toList());
    }
}
