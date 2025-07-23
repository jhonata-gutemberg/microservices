package dev.gutemberg.post.ui.models;

import java.util.UUID;

public record PostSummaryOutput(UUID id, String title, String author, String summary) {
}
