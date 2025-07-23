package dev.gutemberg.post.api.models;

import java.util.UUID;

public record PostSummaryOutput(UUID id, String title, String summary, String author) {
}
