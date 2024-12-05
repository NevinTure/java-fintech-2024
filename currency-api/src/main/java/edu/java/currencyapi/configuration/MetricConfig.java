package edu.java.currencyapi.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

    @Bean
    public Counter requestCounter(MeterRegistry registry) {
        return Counter.builder("request_counter")
                .description("Counter number of requests to Currency API")
                .register(registry);
    }
}
