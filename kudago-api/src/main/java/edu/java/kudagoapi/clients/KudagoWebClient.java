package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.dtos.events.EventsResponse;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface KudagoWebClient {

    Mono<EventsResponse> getEvents(Map<String, String> request);
}
