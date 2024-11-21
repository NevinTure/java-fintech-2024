package edu.java.mqbenchmark;

import edu.java.mqbenchmark.configuration.ApplicationConfig;
import edu.java.mqbenchmark.dtos.KafkaMessage;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 30, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class KafkaBenchmarkTest extends AbstractBenchmark {

    private static ProducerFactory<String, KafkaMessage> producerFactory;
    private static ConsumerFactory<String, KafkaMessage> consumerFactory;
    private static ApplicationConfig config;

    @Autowired
    public void setProducerFactory(ProducerFactory<String, KafkaMessage> producerFactory) {
        KafkaBenchmarkTest.producerFactory = producerFactory;
    }

    @Autowired
    public void setConsumerFactory(ConsumerFactory<String, KafkaMessage> consumerFactory) {
        KafkaBenchmarkTest.consumerFactory = consumerFactory;
    }

    @Autowired
    public void setConfig(ApplicationConfig config) {
        KafkaBenchmarkTest.config = config;
    }


//    @Benchmark
//    public void testSimpleConfig() {
//        Producer<String, KafkaMessage> producer =
//                producerFactory.createProducer();
//        producer.send(new ProducerRecord<>(config.kafka().producer().topicName(), new KafkaMessage("message1")));
//        Consumer<String, KafkaMessage> consumer =
//                consumerFactory.createConsumer(config.kafka().consumer().groupId(), "mq-benchmark");
//        consumer.subscribe(List.of(config.kafka().producer().topicName()));
//        ConsumerRecords<String, KafkaMessage> poll = consumer.poll(Duration.ofMillis(500));
//    }

//    @Benchmark
    public void testLoadBalancing() {
        Producer<String, KafkaMessage> producer1 =
                producerFactory.createProducer();
        Producer<String, KafkaMessage> producer2 =
                producerFactory.createProducer();
        Producer<String, KafkaMessage> producer3 =
                producerFactory.createProducer();
        producer1.send(new ProducerRecord<>(config.kafka().producer().topicName(), new KafkaMessage("message1")));
        producer2.send(new ProducerRecord<>(config.kafka().producer().topicName(), new KafkaMessage("message1")));
        producer3.send(new ProducerRecord<>(config.kafka().producer().topicName(), new KafkaMessage("message1")));
        Consumer<String, KafkaMessage> consumer =
                consumerFactory.createConsumer(config.kafka().consumer().groupId(), "mq-benchmark");
        consumer.subscribe(List.of(config.kafka().producer().topicName()));
        ConsumerRecords<String, KafkaMessage> poll = consumer.poll(Duration.ofSeconds(1));
    }

    @Benchmark
    public void testMultipleConsumers() {
        Producer<String, KafkaMessage> producer =
                producerFactory.createProducer();
        producer.send(new ProducerRecord<>(config.kafka().producer().topicName(), new KafkaMessage("message1")));
        Consumer<String, KafkaMessage> consumer1 =
                consumerFactory.createConsumer(config.kafka().consumer().groupId(), "mq-benchmark");
        consumer1.subscribe(List.of(config.kafka().producer().topicName()));
         consumer1.poll(Duration.ofSeconds(1));
        Consumer<String, KafkaMessage> consumer2 =
                consumerFactory.createConsumer(config.kafka().consumer().groupId(), "mq-benchmark");
        consumer2.subscribe(List.of(config.kafka().producer().topicName()));
        consumer2.poll(Duration.ofSeconds(1));
        Consumer<String, KafkaMessage> consumer3 =
                consumerFactory.createConsumer(config.kafka().consumer().groupId(), "mq-benchmark");
        consumer3.subscribe(List.of(config.kafka().producer().topicName()));
        consumer3.poll(Duration.ofSeconds(1));
    }

}
