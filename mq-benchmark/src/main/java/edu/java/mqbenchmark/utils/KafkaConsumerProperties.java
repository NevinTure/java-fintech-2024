package edu.java.mqbenchmark.utils;

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
