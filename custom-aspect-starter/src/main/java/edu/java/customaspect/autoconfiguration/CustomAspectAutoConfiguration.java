package edu.java.customaspect.autoconfiguration;

import edu.java.customaspect.aspects.SemaphoreRateLimiterAspect;
import edu.java.customaspect.aspects.TimedAspect;
import edu.java.customaspect.config.CustomAspectConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(CustomAspectConfig.class)
public class CustomAspectAutoConfiguration {

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }

    @Bean
    public SemaphoreRateLimiterAspect semaphoreRateLimiterAspect(CustomAspectConfig customAspectConfig) {
        return new SemaphoreRateLimiterAspect(customAspectConfig);
    }
}
