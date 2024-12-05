package edu.java.mqbenchmark.utils.rabbitmq;

public interface RabbitmqProducer<T> {

    void send(String routingKey, T message);
}
