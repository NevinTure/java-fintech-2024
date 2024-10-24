package edu.java.kudagoapi;

import edu.java.kudagoapi.clients.CurrencyClient;
import edu.java.kudagoapi.dtos.CurrencyConvertRequest;
import edu.java.kudagoapi.dtos.CurrencyConvertResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.stream.Stream;

@SpringBootTest(properties = {"custom-aspect.rate-limiter-config.enable=true",
"custom-aspect.rate-limiter-config.acquire-timeout=5ms",
"custom-aspect.rate-limiter-config.permits=3"})
public class RateLimitTest extends IntegrationEnvironment {

    @SpyBean
    private CurrencyClient client;

    @Test
    public void testRateLimit() {
        //given
        CurrencyConvertRequest request = new CurrencyConvertRequest(
                "RUB", "RUB", new BigDecimal(100)
        );

        //when
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            executor.invokeAll(Stream.generate(() -> request).limit(10)
                    .<Callable<CurrencyConvertResponse>>map(v -> () -> client.convert(v))
                    .toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //then
        Mockito.verify(client, Mockito.atMost(3)).convert(request);
    }
}
