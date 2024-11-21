package edu.java.mqbenchmark.utils;

import java.time.Duration;

public record ProducerProperties(
        String bootstrapServers,
        String clientId,
        String acksMode,
        Integer lingerMs,
        Integer batchSize,
        String topicName
) {
}
