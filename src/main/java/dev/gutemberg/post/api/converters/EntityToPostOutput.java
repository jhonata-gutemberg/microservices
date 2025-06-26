package dev.gutemberg.post.api.converters;

import dev.gutemberg.post.api.models.PostOutput;
import dev.gutemberg.post.domain.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToPostOutput implements Converter<Post, PostOutput> {
    @Override
    public PostOutput convert(@NotNull final Post post) {
        return new PostOutput(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getAuthor(),
                post.getWordCount(),
                post.getCalculatedValue()
        );
    }
}
