package edu.java.currencyapi.services;

import edu.java.currencyapi.clients.CbrClient;
import edu.java.currencyapi.dtos.*;
import edu.java.currencyapi.exceptions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CurrencyServiceTest {

    @Autowired
    private CurrencyService service;
    @MockBean
    private CbrClient client;

    @Test
    public void testGetRate() {
        //given
        int code = 36;

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(
                List.of(new CbrCurrency(36, "AUD", new BigDecimal("65.78")))
        ));
        ResponseEntity<CurrencyRateResponse> rate = service.getRate(code);

        //then
        CurrencyRateResponse expected = new CurrencyRateResponse(
                "AUD", new BigDecimal("65.78")
        );
        assertThat(rate.getBody()).isEqualTo(expected);
    }

    @Test
    public void testGetRateWhenNotExists() {
        //given
        int code = 1999;

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(List.of()));

        //then
        assertThatExceptionOfType(BadRequestApiException.class)
                .isThrownBy(() -> service.getRate(code));
    }

    @Test
    public void testGetRateWhenCbNotFound() {
        //given
        int code = 400;

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(List.of()));

        //then
        assertThatExceptionOfType(NotFoundApiException.class)
                .isThrownBy(() -> service.getRate(code));
    }

    @Test
    public void testGetRateWhenServiceUnavailable() {
        //given
        int code = 36;

        //when
        Mockito.when(client.getCurrencies()).thenThrow(ServiceUnavailableApiException.class);

        //then
        assertThatExceptionOfType(ServiceUnavailableApiException.class)
                .isThrownBy(() -> service.getRate(code));
    }

    @Test
    public void testConvert() {
        //given
        CurrencyConvertRequest request = new CurrencyConvertRequest(
                "USD",
                "EUR",
                new BigDecimal(1L)
        );

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(
                List.of(new CbrCurrency(36, "USD", new BigDecimal("30")),
                        new CbrCurrency(36, "EUR", new BigDecimal("54"))
                )));
        ResponseEntity<CurrencyConvertResponse> response = service.convert(request);

        //then
        CurrencyConvertResponse expected = new CurrencyConvertResponse(
                "USD",
                "EUR",
                new BigDecimal("0.5556")
        );
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testConvertWhenNotExists() {
        //given
        CurrencyConvertRequest request = new CurrencyConvertRequest(
                "RUB",
                "TEST",
                new BigDecimal(1L)
        );

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(List.of()));

        //then
        assertThatExceptionOfType(BadRequestApiException.class)
                .isThrownBy(() -> service.convert(request));
    }

    @Test
    public void testConvertWhenCbNotFound() {
        //given
        CurrencyConvertRequest request = new CurrencyConvertRequest(
                "RUB",
                "JOD",
                new BigDecimal(1L)
        );

        //when
        Mockito.when(client.getCurrencies()).thenReturn(new CbrCurrenciesResponse(List.of()));

        //then
        assertThatExceptionOfType(NotFoundApiException.class)
                .isThrownBy(() -> service.convert(request));
    }
}
