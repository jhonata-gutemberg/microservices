package dev.gutemberg.post.api.converters;

import dev.gutemberg.post.api.models.PostSummaryOutput;
import dev.gutemberg.post.domain.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class EntityToPostSummaryOutput implements Converter<Post, PostSummaryOutput> {
    private static final String LINE_SEPARATOR = "\n";
    private static final int SUMMARY_LIMIT = 3;

    @Override
    public PostSummaryOutput convert(@NotNull final Post post) {
        return new PostSummaryOutput(
                post.getId(),
                post.getTitle(),
                summarize(post.getBody()),
                post.getAuthor()
        );
    }

    private String summarize(final String body) {
        return Arrays.stream(body.splitWithDelimiters(LINE_SEPARATOR, SUMMARY_LIMIT + 1))
                .limit(SUMMARY_LIMIT + 2L)
                .collect(Collectors.joining());
    }
}
