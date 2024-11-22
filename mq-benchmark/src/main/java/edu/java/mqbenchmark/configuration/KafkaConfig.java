package edu.java.mqbenchmark.configuration;

import edu.java.mqbenchmark.dtos.KafkaMessage;
import edu.java.mqbenchmark.utils.KafkaConsumerProperties;
import edu.java.mqbenchmark.utils.KafkaProducerProperties;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, KafkaMessage> producerFactory(ApplicationConfig config) {
        KafkaProducerProperties kafka = config.kafka().producer();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, kafka.acksMode());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafka.clientId());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafka.batchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafka.lingerMs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory(ApplicationConfig config) {
        KafkaConsumerProperties kafka = config.kafka().consumer();
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafka.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafka.autoOffsetReset());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafka.maxPollIntervalMs());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaMessage.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate(
            ProducerFactory<String, KafkaMessage> producerFactory,
            ConsumerFactory<String, KafkaMessage> consumerFactory) {
        KafkaTemplate<String, KafkaMessage> template = new KafkaTemplate<>(producerFactory);
        template.setConsumerFactory(consumerFactory);
        return template;
    }

    public ProducerFactory<String, KafkaMessage> producerFactoryWithDefaults() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "mq-benchmark");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 10);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    public ConsumerFactory<String, KafkaMessage> consumerFactoryWithDefaults() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300_000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaMessage.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
