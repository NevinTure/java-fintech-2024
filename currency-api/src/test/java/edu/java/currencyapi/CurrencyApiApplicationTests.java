package edu.java.currencyapi;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.currencyapi.clients.CbrClient;
import edu.java.currencyapi.exceptions.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Currency;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest("app.cbr-base-api=http://localhost:8090")
//@WireMockTest(httpPort = 8090)
class CurrencyApiApplicationTests {

    @Autowired
    CbrClient cbrClient;

    @Test
    void contextLoads() {
        Currency.getInstance("36");
        Currency.getInstance("USD");
//        stubFor(get("/scripts/XML_daily.asp").willReturn(status(404)));
//        cbrClient.getCurrencies();
//        cbrClient.getCurrencies();
//        cbrClient.getCurrencies();
//        cbrClient.getCurrencies();
//        cbrClient.getCurrencies();
//
//        assertThatExceptionOfType(Exception.class).isThrownBy(() ->  cbrClient.getCurrencies());
//        assertThatExceptionOfType(Exception.class).isThrownBy(() ->  cbrClient.getCurrencies());
//        assertThatExceptionOfType(Exception.class).isThrownBy(() ->  cbrClient.getCurrencies());
//        assertThatExceptionOfType(Exception.class).isThrownBy(() ->  cbrClient.getCurrencies());
//        assertThatExceptionOfType(Exception.class).isThrownBy(() ->  cbrClient.getCurrencies());

    }

}
