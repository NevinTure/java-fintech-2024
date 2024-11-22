package edu.java.mqbenchmark.utils.kafka;

public record KafkaConsumerProperties(
        String bootstrapServers,
        String groupId,
        String autoOffsetReset,
        Integer maxPollIntervalMs,
        Boolean enableAutoCommit,
        Integer concurrency,
        String topicName
) {
}
