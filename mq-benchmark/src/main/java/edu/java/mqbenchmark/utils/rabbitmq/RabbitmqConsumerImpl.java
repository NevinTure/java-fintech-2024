package edu.java.mqbenchmark.utils.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.Duration;
import java.util.function.Function;

@RequiredArgsConstructor
public class RabbitmqConsumerImpl<T> implements RabbitmqConsumer<T> {

    private final RabbitTemplate template;
    private final Function<byte[], T> deserializer;

    @Override
    public T receive(Duration timeout) {
        byte[] body = template.receive(timeout.toMillis()).getBody();
        return body == null ? null : deserializer.apply(body);
    }

}
