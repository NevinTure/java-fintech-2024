package edu.java.currencyapi.controllers;

import edu.java.currencyapi.dtos.*;
import edu.java.currencyapi.exceptions.*;
import edu.java.currencyapi.services.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService service;

    @Test
    public void testGetRate() throws Exception {
        //when
        Mockito.when(service.getRate(1)).thenReturn(
                ResponseEntity.ok(new CurrencyRateResponse("test", new BigDecimal(10)))
        );

        //then
        mockMvc.perform(get("/currencies/rates/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRateWhenNotFound() throws Exception {
        //when
        Mockito.when(service.getRate(1))
                .thenThrow(NotFoundApiException.class);

        //then
        mockMvc.perform(get("/currencies/rates/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRateWhenBadRequest() throws Exception {
        //when
        Mockito.when(service.getRate(1))
                .thenThrow(BadRequestApiException.class);

        //then
        mockMvc.perform(get("/currencies/rates/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetRateWhenServiceUnavailable() throws Exception {
        //when
        Mockito.when(service.getRate(1))
                .thenThrow(ServiceUnavailableApiException.class);

        //then
        mockMvc.perform(get("/currencies/rates/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(header().string("Retry-After", "3600"));
    }

    @Test
    public void testConvert() throws Exception {
        //when
        Mockito.when(service.convert(new CurrencyConvertRequest(
                "USD",
                "RUB",
                new BigDecimal(1)
        ))).thenReturn(ResponseEntity.ok(new CurrencyConvertResponse(
                "USD",
                "RUB",
                new BigDecimal(30))));

        //then
        mockMvc.perform(get("/currencies/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fromCurrency": "USD",
                          "toCurrency": "RUB",
                          "amount": 1
                        }""")).andExpect(content().json("""
                {
                  "fromCurrency": "USD",
                  "toCurrency": "RUB",
                  "convertedAmount": 30
                }
                """)).andExpect(status().isOk());
    }

    @Test
    public void testConvertWhenNotFound() throws Exception {
        //when
        Mockito.when(service.convert(new CurrencyConvertRequest(
                        "USD",
                        "RUB",
                        new BigDecimal(1)
                )))
                .thenThrow(NotFoundApiException.class);

        //then
        mockMvc.perform(get("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCurrency": "USD",
                                  "toCurrency": "RUB",
                                  "amount": 1
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testConvertWhenBadRequest() throws Exception {
        //when
        Mockito.when(service.convert(new CurrencyConvertRequest(
                        "USD",
                        "RUB",
                        new BigDecimal(1)
                )))
                .thenThrow(BadRequestApiException.class);

        //then
        mockMvc.perform(get("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCurrency": "USD",
                                  "toCurrency": "RUB",
                                  "amount": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testConvertWhenMissingParam() throws Exception {
        //then
        mockMvc.perform(get("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCurrency": "USD",
                                  "toCurrency": "RUB"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "code": 400,
                          "message": "Invalid request params: amount must not be null"
                        }
                        """));
    }

    @Test
    public void testConvertWhenNegativeAmount() throws Exception {
        //then
        mockMvc.perform(get("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCurrency": "USD",
                                  "toCurrency": "RUB",
                                  "amount": -5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "code": 400,
                          "message": "Invalid request params: amount must be greater than or equal to 1"
                        }
                        """));
    }

    @Test
    public void testConvertWhenServiceUnavailable() throws Exception {
        //when
        Mockito.when(service.convert(new CurrencyConvertRequest(
                "USD",
                "RUB",
                new BigDecimal(1)
        ))).thenThrow(ServiceUnavailableApiException.class);

        //then
        mockMvc.perform(get("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCurrency": "USD",
                                  "toCurrency": "RUB",
                                  "amount": 1
                                }
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(header().string("Retry-After", "3600"));
    }
}
