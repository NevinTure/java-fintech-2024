package edu.java.kudagoapi;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Testcontainers
public class IntegrationEnvironment {

    public static WireMockContainer WIREMOCK;

    static {
        WIREMOCK = new WireMockContainer("wiremock/wiremock:2.35.0")
                .withMappingFromResource("kudago-api", IntegrationEnvironment.class, "kudago_client_mappings.json")
                .withMappingFromResource("currency-api", IntegrationEnvironment.class, "currency_client_mappings.json");
        WIREMOCK.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kudago-api-base-url", () -> "http://localhost:" + WIREMOCK.getPort());
        registry.add("app.currency-api-base-url", () -> "http://localhost:" + WIREMOCK.getPort());
    }
}
