package dev.gutemberg.text.processor.domain.models;

import java.util.UUID;

public record PostOutput(UUID postId, long wordCount, double calculatedValue) {
}
