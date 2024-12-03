package edu.java.mqbenchmark;

import edu.java.mqbenchmark.configuration.KafkaConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class KafkaWithReplicationBenchmarkTest {

    private ProducerFactory<String, String> producerFactory;
    private ConsumerFactory<String, String> consumerFactory;
    private final String topic = "mq-benchmark.benchmark";
    private final String groupId = "benchmark";
    private List<Producer<String, String>> producers;
    private List<Consumer<String, String>> consumers;


    @Setup(Level.Trial)
    public void setup() {
        KafkaConfig kafkaConfig = new KafkaConfig();
        producerFactory = kafkaConfig.producerFactoryWithDefaults(
                Map.of("acks", "all",
                        "bootstrap. servers", "localhost:9092,localhost:9093"));
        consumerFactory = kafkaConfig.consumerFactoryWithDefaults();
        producers = createProducers(10);
        consumers = createConsumers(10, groupId, topic);
    }

    @Benchmark
    public void testSimpleConfig() {
        List<Producer<String, String>> oneProducer = producers.subList(0, 1);
        List<Consumer<String, String>> oneConsumer = consumers.subList(0, 1);
        sendDefaultMessages(oneProducer, topic, 1);
        receiveMessages(oneConsumer, Duration.ofMillis(100), 1);
    }

    @Benchmark
    public void testLoadBalancing() {
        List<Producer<String, String>> threeProducers = producers.subList(0, 3);
        List<Consumer<String, String>> oneConsumer = consumers.subList(0, 1);
        sendDefaultMessages(threeProducers, topic, 3);
        receiveMessages(oneConsumer, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void testMultipleConsumers() {
        List<Producer<String, String>> oneProducer = producers.subList(0, 1);
        List<Consumer<String, String>> threeConsumers = consumers.subList(0, 3);
        sendDefaultMessages(oneProducer, topic, 3);
        receiveMessages(threeConsumers, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void testLoadBalancingMultipleConsumers() {
        List<Producer<String, String>> threeProducers = producers.subList(0, 3);
        List<Consumer<String, String>> threeConsumers = consumers.subList(0, 3);
        sendDefaultMessages(threeProducers, topic, 3);
        receiveMessages(threeConsumers, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void stressTest() {
        List<Producer<String, String>> tenProducers = producers.subList(0, 10);
        List<Consumer<String, String>> tenConsumers = consumers.subList(0, 10);
        sendDefaultMessages(tenProducers, topic, 10);
        receiveMessages(tenConsumers, Duration.ofMillis(100), 10);
    }

    private List<Consumer<String, String>> createConsumers(int count, String groupId, String... topic) {
        List<Consumer<String, String>> consumers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Consumer<String, String> consumer = consumerFactory.createConsumer(groupId, null);
            consumer.subscribe(Arrays.asList(topic));
            consumers.add(consumer);
        }
        return consumers;
    }

    private List<Producer<String, String>> createProducers(int count) {
        List<Producer<String, String>> producers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            producers.add(producerFactory.createProducer());
        }
        return producers;
    }

    private void sendMessages(List<Producer<String, String>> producers, String topic, String message, int times) {
        int n = producers.size();
        for (int i = 0; i < times; i++) {
            producers.get(i % n).send(new ProducerRecord<>(topic, message));
        }
    }

    private void sendDefaultMessages(List<Producer<String, String>> producers, String topic, int times) {
        sendMessages(producers, topic, "message", times);
    }

    private void receiveMessages(List<Consumer<String, String>> consumers, Duration timeout, int times) {
        int n = consumers.size();
        for (int i = 0; i < times; i++) {
            consumers.get(i % n).poll(timeout);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(KafkaWithReplicationBenchmarkTest.class.getSimpleName())
                .forks(1)
                .threads(1)
                .output("kafka-replication-results.txt")
                .build();
        new Runner(opts).run();
    }
}

