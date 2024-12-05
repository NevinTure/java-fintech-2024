package edu.java.mqbenchmark.utils.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitmqProducerImpl<T> implements RabbitmqProducer<T> {

    private final RabbitTemplate template;

    @Override
    public void send(String routingKey, T message) {
        template.convertAndSend(routingKey, message);
    }
}
