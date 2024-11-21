package edu.java.mqbenchmark.utils;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public record KafkaProperties(
        @NestedConfigurationProperty
        ProducerProperties producer,
        @NestedConfigurationProperty
        ConsumerProperties consumer
) {
}
