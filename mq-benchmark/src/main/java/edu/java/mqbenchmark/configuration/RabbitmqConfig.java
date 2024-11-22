package edu.java.mqbenchmark.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {

    private final ApplicationConfig config;

    @Bean
    public Queue queue() {
        return new Queue(config.rabbitmq().consumer().queue(), true);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(config.rabbitmq().producer().exchange(), true, false);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(config.rabbitmq().routingKey())
                .noargs();
    }
}
