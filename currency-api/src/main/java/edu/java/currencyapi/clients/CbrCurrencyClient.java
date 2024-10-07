package edu.java.currencyapi.clients;

import edu.java.currencyapi.dtos.CbrCurrenciesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cbr-currency", url = "${app.cbr-base-api}")
public interface CbrCurrencyClient {

    @GetMapping("/scripts/XML_daily.asp")
    ResponseEntity<CbrCurrenciesResponse> getCbrCurrencies();
}
