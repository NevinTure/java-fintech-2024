package edu.java.kudagoapi.utils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record Token(UUID id, String subject,
                    List<String> authorities,
                    OffsetDateTime createdAt,
                    OffsetDateTime expiresAt) {
}
