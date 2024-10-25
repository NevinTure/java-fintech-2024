package edu.java.kudagoapi.services;

import edu.java.kudagoapi.clients.*;
import edu.java.kudagoapi.dtos.CurrencyConvertRequest;
import edu.java.kudagoapi.dtos.CurrencyConvertResponse;
import edu.java.kudagoapi.dtos.events.EventDto;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.repositories.JpaEventRepository;
import edu.java.kudagoapi.utils.PriceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements ConcurrentEventService {

    private final CurrencyClient currencyClient;
    private final KudagoClient kudagoClient;
    private final KudagoWebClient kudagoWebClient;
    private final JpaEventRepository repo;
    private static final String RUBLE = "RUB";
    private static final int PAGE_SIZE = 1000;

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
        request.put("actual_since", (from == null ? LocalDate.ofEpochDay(0) : from).toString());
        request.put("actual_until", (to == null ? LocalDate.ofEpochDay(1_000_000) : to).toString());
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

    @Override
    public ResponseEntity<Object> save(EventDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<EventDto> getById(long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<EventDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<Object> fullUpdate(long id, EventDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<Object> deleteById(long id) {
        return null;
    }
}
