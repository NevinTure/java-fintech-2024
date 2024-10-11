package edu.java.kudagoapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        String kudagoApiBaseUrl,
        String apiVersion,
        Integer poolSize,
        Duration updateDelay
) {

}
