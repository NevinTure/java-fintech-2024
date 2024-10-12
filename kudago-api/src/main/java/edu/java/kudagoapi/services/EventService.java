package edu.java.kudagoapi.services;

import edu.java.kudagoapi.clients.CurrencyClient;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.CurrencyConvertRequest;
import edu.java.kudagoapi.dtos.CurrencyConvertResponse;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.utils.PriceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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

        CurrencyConvertRequest currencyRequest = new CurrencyConvertRequest(
                currency,
                RUBLE,
                budget
        );
        Map<String, String> request = createEventsRequestWithDefaults(from, to);

        CompletableFuture<CurrencyConvertResponse> currencyResponse =
                CompletableFuture.supplyAsync(
                        () -> currencyClient.convert(currencyRequest));
        CompletableFuture<EventsResponse> eventsResponse =
                CompletableFuture.supplyAsync(
                        () -> kudagoClient.getEvents(request)
                );
        eventsResponse
                .thenAcceptBoth(currencyResponse,
                        (evReq, curReq) -> evReq.setEvents(evReq
                                .getEvents()
                                .stream()
                                .filter(v -> PriceParser
                                        .toBigDecimal(v.getPrice()).compareTo(curReq.getConvertedAmount()) <= 0)
                                .toList())
                );
        return eventsResponse;
    }

    private Map<String, String> createEventsRequestWithDefaults(LocalDate from, LocalDate to) {
        Map<String, String> request = new HashMap<>();
        request.put("fields", "id,place,location,price");
        request.put("expand", "place,location");
        request.put("actual_since", (from == null ? LocalDate.MIN : from).toString());
        request.put("actual_until", (to == null ? LocalDate.MAX : to).toString());
        request.put("page_size", "100");
        return request;
    }
}
