package edu.java.currencyapi.clients;

import edu.java.currencyapi.dtos.CbrCurrenciesResponse;
import edu.java.currencyapi.exceptions.ApiException;
import edu.java.currencyapi.exceptions.ServiceUnavailableApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.io.FileNotFoundException;
import javax.naming.ServiceUnavailableException;

@Component
@RequiredArgsConstructor
public class CbrRestClient implements CbrClient {

    private final RestClient cbrClient;

    @Override
    @CircuitBreaker(name = "cbr-client", fallbackMethod = "circuitBreakerFallback")
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
}
