package edu.java.currencyapi.clients;

import edu.java.currencyapi.dtos.CbrCurrenciesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CbrRestClient implements CbrClient {

    private final RestClient cbrClient;

    @Override
    public CbrCurrenciesResponse getCurrencies() {
        return cbrClient
                .get()
                .uri("/scripts/XML_daily.asp")
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(CbrCurrenciesResponse.class);
    }
}
