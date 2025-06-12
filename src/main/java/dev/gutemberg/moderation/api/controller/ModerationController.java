package dev.gutemberg.moderation.api.controller;

import dev.gutemberg.moderation.api.model.ModerationInput;
import dev.gutemberg.moderation.api.model.ModerationOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/api/moderate")
public class ModerationController {
    private static final Set<String> FORBIDDEN_WORDS = Set.of("hate", "curse");

    @PostMapping
    ModerationOutput moderate(@RequestBody final ModerationInput moderationInput) {
        for (final var forbiddenWord: FORBIDDEN_WORDS) {
            if (moderationInput.text().contains(forbiddenWord)) {
                return new ModerationOutput(false, "the text contains " + forbiddenWord);
            }
        }
        return new ModerationOutput(true, "the text does not contains any forbidden words");
    }
}
