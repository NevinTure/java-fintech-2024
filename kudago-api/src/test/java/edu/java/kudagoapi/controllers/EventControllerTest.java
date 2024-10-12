package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.clients.CurrencyClient;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.*;
import edu.java.kudagoapi.dtos.events.EventsRequest;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import edu.java.kudagoapi.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest extends IntegrationEnvironment {

    @Autowired
    private KudagoClient kudagoClient;
    @Autowired
    private CurrencyClient currencyClient;
    @Autowired
    private EventService eventService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/events/future")
                        .queryParam("budget", "100")
                        .queryParam("currency", "RUB")
                        .queryParam("dateFrom", "2024-02-12")
                        .queryParam("dateTo", "2024-10-12"))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                        .andExpect(status().isOk())
                                .andDo(print());
    }

    @Test
    public void test2() throws Exception {
        LocalDate from = LocalDate.of(2024, 2, 12);
        LocalDate to = LocalDate.of(2024, 10, 12);
        Map<String, String> request = new HashMap<>();
        request.put("fields", "id,place,location,price");
        request.put("expand", "place,location");
        request.put("actual_since", from.toString());
        request.put("actual_until", to.toString());
        request.put("page_size", "100");
        EventsResponse events = kudagoClient.getEvents(request);
        System.out.println(events);

    }

    @Test
    public void test3() {
        CurrencyConvertRequest request = new CurrencyConvertRequest(
                "RUB",
                "RUB",
                new BigDecimal(100)
        );
        CurrencyConvertResponse response = currencyClient.convert(request);
        System.out.println(response);
    }
}
//?expand=place%2Clocation&fields=id%2Cplace%2Clocation%2Cprice&page_size=100&actual_since=2024-02-12&actual_until=2024-10-12"
