package edu.java.currencyapi.controllers;

import edu.java.currencyapi.dtos.*;
import edu.java.currencyapi.services.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Validated
public class CurrencyController {

    private final CurrencyService service;

    @GetMapping("/rates/{rate}")
    public ResponseEntity<CurrencyRateResponse> getRate(@PathVariable("rate") int rate) {
        return service.getRate(rate);
    }

    @GetMapping("/convert")
    public ResponseEntity<CurrencyConvertResponse> convert(@RequestBody @Valid CurrencyConvertRequest request) {
        return service.convert(request);
    }
}
