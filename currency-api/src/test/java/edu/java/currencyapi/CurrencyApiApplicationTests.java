package edu.java.currencyapi;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.currencyapi.clients.CbrClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

//@SpringBootTest("app.cbr-base-api=http://localhost:8080")
//@WireMockTest(httpPort = 8080)
@SpringBootTest
class CurrencyApiApplicationTests {

    @Autowired
    CbrClient cbrClient;

    @Test
    void contextLoads() {
        System.out.println(cbrClient.getCurrencies().getCurrencies());
//        stubFor(get("/scripts/XML_daily.asp").willReturn(status(500)));
    }

}
