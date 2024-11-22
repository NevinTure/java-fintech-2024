package edu.java.mqbenchmark.utils;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public record KafkaProperties(
        @NestedConfigurationProperty
        KafkaProducerProperties producer,
        @NestedConfigurationProperty
        KafkaConsumerProperties consumer
) {
}
