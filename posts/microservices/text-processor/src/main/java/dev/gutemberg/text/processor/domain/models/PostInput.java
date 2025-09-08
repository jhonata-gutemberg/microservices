package dev.gutemberg.text.processor.domain.models;

import java.util.UUID;

public record PostInput(UUID postId, String postBody) {
}
