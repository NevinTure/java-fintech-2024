package edu.java.currencyapi.services;

import edu.java.currencyapi.dtos.*;
import org.springframework.http.ResponseEntity;

public interface CurrencyService {

    ResponseEntity<CurrencyRateResponse> getRate(Integer code);

    ResponseEntity<CurrencyConvertResponse> convert(CurrencyConvertRequest request);
}
