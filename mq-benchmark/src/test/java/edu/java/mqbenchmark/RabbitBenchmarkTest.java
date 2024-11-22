//package edu.java.mqbenchmark;
//
//import edu.java.mqbenchmark.configuration.ApplicationConfig;
//import edu.java.mqbenchmark.dtos.KafkaMessage;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.producer.Producer;
//import org.openjdk.jmh.annotations.*;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import java.time.Duration;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@SpringBootTest("logging.pattern.console=")
//@State(Scope.Thread)
//@Warmup(iterations = 5, time = 1)
//@Measurement(iterations = 30, time = 1)
//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.SECONDS)
//public class RabbitBenchmarkTest extends AbstractBenchmark {
//
//    private static ApplicationConfig config;
//    private static ConnectionFactory connectionFactory;
//
//    @Autowired
//    public void setConfig(ApplicationConfig config) {
//        RabbitBenchmarkTest.config = config;
//    }
//
//    @Autowired
//    public void setConnectionFactory(ConnectionFactory connectionFactory) {
//        new RabbitTemplate().receive()
//        RabbitBenchmarkTest.connectionFactory = connectionFactory;
//        connectionFactory.createConnection().createChannel(false).basicConsume()
//    }
//
//    public void testSimpleConfig() {
//
//    }
//}
