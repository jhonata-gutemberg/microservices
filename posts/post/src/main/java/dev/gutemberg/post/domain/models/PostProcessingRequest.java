package dev.gutemberg.post.domain.models;

import java.util.UUID;

public record PostProcessingRequest(UUID postId, String postBody) {
}
