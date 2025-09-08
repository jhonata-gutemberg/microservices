package dev.gutemberg.comment.api.client;

import dev.gutemberg.comment.api.model.ModerationInput;
import dev.gutemberg.comment.api.model.ModerationOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/moderate")
public interface ModerationClient {
    @PostExchange
    ModerationOutput moderate(@RequestBody ModerationInput moderationInput);
}
