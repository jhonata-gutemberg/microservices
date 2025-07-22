package dev.gutemberg.post.api.converters;

import dev.gutemberg.post.api.models.PostInput;
import dev.gutemberg.post.domain.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostInputToEntityConverter implements Converter<PostInput, Post> {
    @Override
    public Post convert(@NotNull final PostInput postInput) {
        return new Post(postInput.title(), postInput.body(), postInput.author());
    }
}
