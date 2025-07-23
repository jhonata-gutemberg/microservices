package dev.gutemberg.moderation.api.model;

import java.util.UUID;

public record ModerationInput(String text, UUID commentId) {
}
