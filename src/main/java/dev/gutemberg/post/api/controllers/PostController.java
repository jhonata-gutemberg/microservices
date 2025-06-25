package dev.gutemberg.post.api.controllers;

import dev.gutemberg.post.api.models.PostInput;
import dev.gutemberg.post.api.models.PostOutput;
import dev.gutemberg.post.api.models.PostSummaryOutput;
import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.domain.services.PostService;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final ConversionService conversionService;

    public PostController(final PostService postService, final ConversionService conversionService) {
        this.postService = postService;
        this.conversionService = conversionService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    PostOutput create(@RequestBody final PostInput postInput) {
        var post = Optional.ofNullable(conversionService.convert(postInput, Post.class)).orElseThrow();
        post = postService.create(post);
        return conversionService.convert(post, PostOutput.class);
    }

    @GetMapping("/{postId}")
    PostOutput getOne(@PathVariable final UUID postId) {
        return postService.getOne(postId)
                .map(post -> conversionService.convert(post, PostOutput.class))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    Page<PostSummaryOutput> search(@PageableDefault final Pageable pageable) {
        return postService.search(pageable)
                .map(post -> conversionService.convert(post, PostSummaryOutput.class));
    }
}
