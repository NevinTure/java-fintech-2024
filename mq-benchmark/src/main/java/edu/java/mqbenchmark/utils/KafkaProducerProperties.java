package edu.java.mqbenchmark.utils;

public record KafkaProducerProperties(
        String bootstrapServers,
        String clientId,
        String acksMode,
        Integer lingerMs,
        Integer batchSize,
        String topicName
) {
}
