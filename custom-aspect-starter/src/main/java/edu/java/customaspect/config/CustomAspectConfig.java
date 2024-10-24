package edu.java.customaspect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "custom-aspect", ignoreUnknownFields = false)
public record CustomAspectConfig(
        RateLimiterConfig rateLimiterConfig
) {

    public record RateLimiterConfig(
            Boolean enable,
            Integer permits,
            Duration acquireTimeout
    ) {}
}

