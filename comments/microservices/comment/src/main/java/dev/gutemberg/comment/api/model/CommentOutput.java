package dev.gutemberg.comment.api.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentOutput(UUID id, String text, String author, OffsetDateTime createdAt) {
}
