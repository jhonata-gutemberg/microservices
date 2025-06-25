package dev.gutemberg.post.api.controllers;

import dev.gutemberg.post.api.models.PostInput;
import dev.gutemberg.post.api.models.PostOutput;
import dev.gutemberg.post.api.models.PostSummaryOutput;
import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.domain.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final String LINE_SEPARATOR = "\n";
    private static final int SUMMARY_LIMIT = 3;

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    PostOutput create(@RequestBody final PostInput postInput) {
        var post = convertToEntity(postInput);
        post = postService.create(post);
        return convertToOutput(post);
    }

    @GetMapping("/{postId}")
    PostOutput getOne(@PathVariable final UUID postId) {
        return postService.getOne(postId)
                .map(this::convertToOutput)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    Page<PostSummaryOutput> search(@PageableDefault final Pageable pageable) {
        return postService.search(pageable).map(this::summarize);
    }

    private Post convertToEntity(final PostInput postInput) {
        return new Post(postInput.title(), postInput.body(), postInput.author());
    }

    private PostOutput convertToOutput(final Post post) {
        return new PostOutput(
                post.id(),
                post.title(),
                post.body(),
                post.author(),
                post.wordCount(),
                post.calculatedValue()
        );
    }

    private PostSummaryOutput summarize(final Post post) {
        return new PostSummaryOutput(
                post.id(),
                post.title(),
                buildSummary(post.body()),
                post.author()
        );
    }

    private String buildSummary(final String body) {
        return Arrays.stream(body.splitWithDelimiters(LINE_SEPARATOR, SUMMARY_LIMIT + 1))
                .limit(SUMMARY_LIMIT + 2L)
                .collect(Collectors.joining());
    }
}
