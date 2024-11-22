package edu.java.mqbenchmark;

import edu.java.mqbenchmark.configuration.RabbitmqConfig;
import edu.java.mqbenchmark.utils.rabbitmq.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class RabbitmqBenchmarkTest {

    private RabbitTemplate template;
    private GenericApplicationContext context;
    private final String routingKey = "benchmark";
    private List<RabbitmqProducer<String>> producers;
    private List<RabbitmqConsumer<String>> consumers;

    @Setup(Level.Trial)
    public void setup() {
        context = createContext();
        template = context.getBean(RabbitTemplate.class);
        producers = createProducerWrappers(10);
        consumers = createConsumerWrappers(10);
    }

    @TearDown
    public void close() {
        context.close();
    }

    @Benchmark
    public void testSimpleConfig() {
        List<RabbitmqProducer<String>> oneProducer = producers.subList(0, 1);
        List<RabbitmqConsumer<String>> oneConsumer = consumers.subList(0, 1);
        sendDefaultMessages(oneProducer, routingKey, 1);
        receiveMessages(oneConsumer, Duration.ofMillis(100), 1);
    }

    @Benchmark
    public void testLoadBalancing() {
        List<RabbitmqProducer<String>> threeProducers = producers.subList(0, 3);
        List<RabbitmqConsumer<String>> oneConsumer = consumers.subList(0, 1);
        sendDefaultMessages(threeProducers, routingKey, 3);
        receiveMessages(oneConsumer, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void testMultipleConsumers() {
        List<RabbitmqProducer<String>> oneProducer = producers.subList(0, 1);
        List<RabbitmqConsumer<String>> threeConsumers = consumers.subList(0, 3);
        sendDefaultMessages(oneProducer, routingKey, 3);
        receiveMessages(threeConsumers, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void testLoadBalancingMultipleConsumers() {
        List<RabbitmqProducer<String>> threeProducers = producers.subList(0, 3);
        List<RabbitmqConsumer<String>> threeConsumers = consumers.subList(0, 3);
        sendDefaultMessages(threeProducers, routingKey, 3);
        receiveMessages(threeConsumers, Duration.ofMillis(100), 3);
    }

    @Benchmark
    public void stressTest() {
        List<RabbitmqProducer<String>> tenProducers = producers.subList(0, 10);
        List<RabbitmqConsumer<String>> tenConsumers = consumers.subList(0, 10);
        sendDefaultMessages(tenProducers, routingKey, 10);
        receiveMessages(tenConsumers, Duration.ofMillis(100), 10);
    }

    private GenericApplicationContext createContext() {
        return new AnnotationConfigApplicationContext(RabbitmqConfig.class);
    }

    public List<RabbitmqProducer<String>> createProducerWrappers(int count) {
        List<RabbitmqProducer<String>> producers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            producers.add(new RabbitmqProducerImpl<>(template));
        }
        return producers;
    }

    public List<RabbitmqConsumer<String>> createConsumerWrappers(int count) {
        List<RabbitmqConsumer<String>> consumers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            consumers.add(new RabbitmqConsumerImpl<>(template, v -> new String(v, StandardCharsets.UTF_8)));
        }
        return consumers;
    }

    private void sendMessages(List<RabbitmqProducer<String>> producers, String routingKey, String message, int times) {
        int n = producers.size();
        for (int i = 0; i < times; i++) {
            producers.get(i % n).send(routingKey, message);
        }
    }

    private void sendDefaultMessages(List<RabbitmqProducer<String>> producers, String routingKey, int times) {
        sendMessages(producers, routingKey, "message", times);
    }

    private void receiveMessages(List<RabbitmqConsumer<String>> consumers, Duration timeout, int times) {
        int n = consumers.size();
        for (int i = 0; i < times; i++) {
            consumers.get(i % n).receive(timeout);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(RabbitmqBenchmarkTest.class.getSimpleName())
                .forks(1)
                .threads(1)
                .output("rabbitmq-results.txt")
                .build();
        new Runner(opts).run();
    }
}
