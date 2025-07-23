package dev.gutemberg.post.ui.converters;

import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.ui.models.PostInput;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostInputToEntity implements Converter<PostInput, Post> {
    @Override
    public Post convert(@NotNull final PostInput postInput) {
        return new Post(postInput.getTitle(), postInput.getMessage(), postInput.getAuthor());
    }
}
