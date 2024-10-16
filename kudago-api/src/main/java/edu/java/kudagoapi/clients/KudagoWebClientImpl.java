package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.dtos.events.EventsResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KudagoWebClientImpl implements KudagoWebClient {

    private final WebClient kudagoWebClient;

    @Override
    public Mono<EventsResponse> getEvents(Map<String, String> request) {
        return kudagoWebClient
                .get()
                .uri(uriBuilder -> buildUriWithQueryParams(uriBuilder.path("/events"), request))
                .retrieve()
                .bodyToMono(EventsResponse.class);
    }

    @SneakyThrows
    private URI buildUriWithQueryParams(UriBuilder builder, Map<String, String> params) {
        for (var entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue().replace(",", "%2C"));
        }
        return builder.build();
    }
}
