package edu.java.mqbenchmark.configuration;

import edu.java.mqbenchmark.utils.kafka.KafkaProperties;
import edu.java.mqbenchmark.utils.rabbitmq.RabbitmqProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Validated
public record ApplicationConfig(
    @NestedConfigurationProperty
    KafkaProperties kafka,
    @NestedConfigurationProperty
    RabbitmqProperties rabbitmq
) {
}
