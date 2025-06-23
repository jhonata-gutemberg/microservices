package dev.gutemberg.post.api.models;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record PostOutput(
        UUID id,
        String title,
        String body,
        String author,
        Optional<Long> wordCount,
        Optional<BigDecimal> calculatedValue
) {
}
