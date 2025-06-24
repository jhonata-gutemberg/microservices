package dev.gutemberg.post.api.controllers;

import dev.gutemberg.post.api.models.PostInput;
import dev.gutemberg.post.api.models.PostOutput;
import dev.gutemberg.post.api.models.PostSummaryOutput;
import dev.gutemberg.post.domain.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    PostOutput create(@RequestBody final PostInput postInput) {
        return postService.create(postInput);
    }

    @GetMapping("/{postId}")
    PostOutput getOne(@PathVariable final UUID postId) {
        return postService.getOne(postId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    Page<PostSummaryOutput> search(@PageableDefault final Pageable pageable) {
        return postService.search(pageable);
    }
}
