package edu.java.kudagoapi;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Testcontainers
public class IntegrationEnvironment {

    public static WireMockContainer WIREMOCK;
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        WIREMOCK = new WireMockContainer("wiremock/wiremock:2.35.0")
                .withMappingFromResource("kudago-api", IntegrationEnvironment.class, "kudago_client_mappings.json")
                .withMappingFromResource("currency-api", IntegrationEnvironment.class, "currency_client_mappings.json");
        WIREMOCK.start();

        POSTGRES = new PostgreSQLContainer<>("postgres:17")
                .withDatabaseName("kudago")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kudago-api-base-url", () -> "http://localhost:" + WIREMOCK.getPort());
        registry.add("app.currency-api-base-url", () -> "http://localhost:" + WIREMOCK.getPort());
        registry.add("spring.datasource.url", () -> POSTGRES.getJdbcUrl());
        registry.add("spring.datasource.username", () -> POSTGRES.getUsername());
        registry.add("spring.datasource.password", () -> POSTGRES.getPassword());
        registry.add("spring.datasource.driver-class-name", () -> POSTGRES.getDriverClassName());
    }
}
