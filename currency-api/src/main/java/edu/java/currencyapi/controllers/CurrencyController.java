package edu.java.currencyapi.controllers;

import edu.java.currencyapi.dtos.*;
import edu.java.currencyapi.services.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/rates/{code}")
    @Operation(summary = "Get currency rate by int code")
    public ResponseEntity<CurrencyRateResponse> getRate(@PathVariable("code") int code) {
        return service.getRate(code);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert between currencies")
    public ResponseEntity<CurrencyConvertResponse> convert(@RequestBody @Valid CurrencyConvertRequest request) {
        return service.convert(request);
    }
}
