package edu.java.currencyapi.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.currencyapi.dtos.CbrCurrenciesResponse;
import edu.java.currencyapi.dtos.CbrCurrency;
import edu.java.currencyapi.exceptions.ServiceUnavailableApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest("app.cbr-base-api=http://localhost:8090")
@WireMockTest(httpPort = 8090)
public class CbrClientTest {

    @Autowired
    private CbrClient client;

    @Test
    public void testGetCurrencies() {
        //when
        stubFor(get("/scripts/XML_daily.asp")
                .willReturn(okXml("""
                        <ValCurs>
                        <Valute>
                        <NumCode>036</NumCode>
                        <CharCode>AUD</CharCode>
                        <VunitRate>65,31</VunitRate>
                        </Valute>
                        <Valute>
                        <NumCode>944</NumCode>
                        <CharCode>AZN</CharCode>
                        <VunitRate>56,91</VunitRate>
                        </Valute>
                        </ValCurs>
                        """)));
        CbrCurrenciesResponse response = client.getCurrencies();

        //then
        CbrCurrenciesResponse expected = new CbrCurrenciesResponse(
                List.of(new CbrCurrency(36, "AUD", new BigDecimal("65.31")),
                        new CbrCurrency(944, "AZN", new BigDecimal("56.91")))
        );
        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void testGetCurrenciesWhenServiceUnavailable() {
        //when
        stubFor(get("/scripts/XML_daily.asp").willReturn(status(500)));

        //then
        assertThatExceptionOfType(ServiceUnavailableApiException.class)
                .isThrownBy(() -> client.getCurrencies());
    }
}
