package edu.java.currencyapi.clients;

import edu.java.currencyapi.dtos.CbrCurrenciesResponse;
import edu.java.currencyapi.exceptions.ServiceUnavailableApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CbrRestClient implements CbrClient {

    private final RestClient cbrClient;

    @Override
    @CircuitBreaker(name = "cbr-client", fallbackMethod = "circuitBreakerFallback")
    @Cacheable("cbr-client")
    public CbrCurrenciesResponse getCurrencies() {
        return cbrClient
                .get()
                .uri("/scripts/XML_daily.asp")
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(CbrCurrenciesResponse.class);
    }

    public CbrCurrenciesResponse circuitBreakerFallback(Exception e) {
        throw new ServiceUnavailableApiException("Service Unavailable");
    }

    @CacheEvict(value = "cbr-client", allEntries = true)
    @Scheduled(fixedRateString = "${app.cache-ttl}")
    public void updateCache() {
        log.info("Cache deleted!");
    }
}
