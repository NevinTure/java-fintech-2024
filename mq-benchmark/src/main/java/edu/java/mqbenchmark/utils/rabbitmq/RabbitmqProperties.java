package edu.java.mqbenchmark.utils.rabbitmq;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public record RabbitmqProperties(
        String routingKey,
        @NestedConfigurationProperty
        RabbitmqProducerProperties producer,
        @NestedConfigurationProperty
        RabbitmqConsumerProperties consumer) {

}
