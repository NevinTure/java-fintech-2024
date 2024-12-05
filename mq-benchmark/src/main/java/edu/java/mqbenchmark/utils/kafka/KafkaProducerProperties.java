package edu.java.mqbenchmark.utils.kafka;

public record KafkaProducerProperties(
        String bootstrapServers,
        String clientId,
        String acksMode,
        Integer lingerMs,
        Integer batchSize,
        String topicName
) {
}
