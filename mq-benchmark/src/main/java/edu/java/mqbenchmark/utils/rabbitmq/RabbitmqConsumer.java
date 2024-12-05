package edu.java.mqbenchmark.utils.rabbitmq;

import java.time.Duration;

public interface RabbitmqConsumer<T> {

    T receive(Duration timeout);
}
