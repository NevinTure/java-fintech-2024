package edu.java.kudagoapi.configuration;

import edu.java.kudagoapi.utils.AccessType;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @DefaultValue("https://kudago.com/public-api/v1.4") String kudagoApiBaseUrl,
        @DefaultValue("http://localhost:8070") String currencyApiBaseUrl,
        @DefaultValue("v1.4") String apiVersion,
        @DefaultValue("5") Integer poolSize,
        @DefaultValue("1h") Duration updateDelay,
        @DefaultValue("false") Boolean enableUpdate,
        @DefaultValue("jpa") AccessType databaseAccessType,
        @NotNull TokenAuthentication tokenAuthentication
) {

   public record TokenAuthentication(String cookieTokenKey, Duration ttl, Duration rememberMeTtl) {}
}
