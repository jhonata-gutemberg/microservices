package dev.gutemberg.comment.api.model;

import java.util.UUID;

public record ModerationInput(String text, UUID commentId) {
}
