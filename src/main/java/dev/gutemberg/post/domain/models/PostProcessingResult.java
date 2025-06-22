package dev.gutemberg.post.domain.models;

import java.util.UUID;

public record PostProcessingResult(UUID postId, long wordCount, double calculatedValue) {
}
