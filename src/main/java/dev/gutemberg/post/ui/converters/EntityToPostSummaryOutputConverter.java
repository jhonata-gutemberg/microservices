package dev.gutemberg.post.ui.converters;

import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.ui.models.PostSummaryOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
@Qualifier("uiEntityToPostSummaryOutputConverter")
public class EntityToPostSummaryOutputConverter implements Converter<Post, PostSummaryOutput> {
    private static final int SUMMARY_MAX_LIMIT = 200;

    @Override
    public PostSummaryOutput convert(final Post post) {
        return new PostSummaryOutput(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                summarize(post.getBody())
        );
    }

    private String summarize(final String body) {
        if (body.length() <= SUMMARY_MAX_LIMIT) {
            return body;
        }
        return body.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .limit(SUMMARY_MAX_LIMIT)
                .collect(Collectors.joining())
                .concat("...");
    }
}
