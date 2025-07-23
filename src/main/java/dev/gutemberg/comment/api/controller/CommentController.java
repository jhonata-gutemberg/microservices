package dev.gutemberg.comment.api.controller;

import dev.gutemberg.comment.api.client.ModerationClient;
import dev.gutemberg.comment.api.model.CommentInput;
import dev.gutemberg.comment.api.model.CommentOutput;
import dev.gutemberg.comment.api.model.ModerationInput;
import dev.gutemberg.comment.domain.model.Comment;
import dev.gutemberg.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ModerationClient moderationClient;
    private final CommentRepository commentRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentOutput create(@RequestBody final CommentInput commentInput) {
        var comment = new Comment(
                UUID.randomUUID(),
                commentInput.text(),
                commentInput.author(),
                OffsetDateTime.now()
        );
        final var moderationInput = new ModerationInput(comment.getText(), comment.getId());
        final var moderationOutput = moderationClient.moderate(moderationInput);
        if (!moderationOutput.approved()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, moderationOutput.reason());
        }
        comment = commentRepository.save(comment);
        return convertToModel(comment);
    }

    @GetMapping("/{commentId}")
    CommentOutput getOne(@PathVariable final UUID commentId) {
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToModel(comment);
    }

    @GetMapping
    Page<CommentOutput> search(@PageableDefault Pageable pageable) {
        return commentRepository.findAll(pageable).map(this::convertToModel);
    }

    private CommentOutput convertToModel(final Comment comment) {
        return new CommentOutput(
                comment.getId(),
                comment.getText(),
                comment.getAuthor(),
                comment.getCreatedAt()
        );
    }
}
