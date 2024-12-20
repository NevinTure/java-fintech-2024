package edu.java.kudagoapi.services.event;

import edu.java.kudagoapi.clients.*;
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
public class EventCollectorService {

    private final CurrencyClient currencyClient;
    private final KudagoClient kudagoClient;
    private final KudagoWebClient kudagoWebClient;
    private static final String RUBLE = "RUB";
    private static final int PAGE_SIZE = 1000;
    private static final LocalDate START_DATE = LocalDate.ofEpochDay(0);
    private static final LocalDate INF_DATE = LocalDate.ofEpochDay(1_000_000);

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
                        () -> kudagoClient.getEvents(eventsRequest));
        return eventsResponse
                .thenCombine(currencyResponse, this::createFilteredEventsResponse);
    }

    public Mono<EventsResponse> getEventsWithReactor(
            BigDecimal budget, String currency, LocalDate from, LocalDate to) {
        CurrencyConvertRequest convertRequest = createConvertRequestWithDefaults(budget, currency);
        Map<String, String> eventsRequest = createEventsRequestWithDefaults(from, to);
        Mono<CurrencyConvertResponse> curConvMono = Mono
                .fromSupplier(() -> currencyClient.convert(convertRequest));
        return kudagoWebClient.getEvents(eventsRequest)
                .zipWith(curConvMono, this::createFilteredEventsResponse);
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
        request.put("actual_since", (from == null ? START_DATE : from).toString());
        request.put("actual_until", (to == null ? INF_DATE : to).toString());
        request.put("fields", "id,place,location,price");
        request.put("expand", "place,location");
        request.put("page_size", String.valueOf(PAGE_SIZE));
        return request;
    }

    private EventsResponse createFilteredEventsResponse(EventsResponse evRes, CurrencyConvertResponse curRes) {
        return new EventsResponse(evRes
                .getEvents()
                .stream()
                .filter(v -> PriceParser
                        .toBigDecimal(v.getPrice()).compareTo(curRes.getConvertedAmount()) <= 0)
                .toList());
    }
}
