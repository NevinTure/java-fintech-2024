package edu.java.mqbenchmark.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {

    @Value("${app.rabbitmq.consumer.queue:benchmark}")
    public String queue;

    @Value("${app.rabbitmq.producer.exchange:mq-benchmark}")
    public String exchange;

    @Value("${app.rabbitmq.routing-key:benchmark}")
    public String routingKey;

    @Value("${spring.rabbitmq.host:localhost}")
    public String hostname;

    @Value("${spring.rabbitmq.virtual-host:vhost}")
    public String vhost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(hostname);
        factory.setVirtualHost(vhost);
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        return factory;
    }

    @Bean
    public AmqpAdmin rabbitmqAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setDefaultReceiveQueue(queue);
        return rabbitTemplate;
    }
}
