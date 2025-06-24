package dev.gutemberg.post.domain.services;

import dev.gutemberg.post.api.models.PostInput;
import dev.gutemberg.post.api.models.PostOutput;
import dev.gutemberg.post.api.models.PostSummaryOutput;
import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.domain.models.PostProcessingRequest;
import dev.gutemberg.post.domain.models.PostProcessingResult;
import dev.gutemberg.post.domain.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.gutemberg.post.infrastructure.rabbitmq.RabbitMQConfig.POST_PROCESSING_EXCHANGE;

@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
    private static final String LINE_SEPARATOR = "\n";
    private static final int SUMMARY_LIMIT = 3;

    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    public PostService(final PostRepository postRepository, final RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public PostOutput create(final PostInput postInput) {
        var post = new Post(postInput.title(), postInput.body(), postInput.author());
        post = postRepository.saveAndFlush(post);
        final var message = new PostProcessingRequest(post.id(), post.body());
        rabbitTemplate.convertAndSend(POST_PROCESSING_EXCHANGE, "", message);
        return convert(post);
    }

    @Transactional
    public Optional<PostOutput> getOne(final UUID postId) {
        return postRepository.findById(postId).map(this::convert);
    }

    @Transactional
    public void processResult(final PostProcessingResult postProcessingResult) {
        final var postId = postProcessingResult.postId();
        LOGGER.info("processing post processing result. postId: {}", postId);
        postRepository.findById(postId)
                .ifPresentOrElse(post -> {
                    post.setWordCount(postProcessingResult.wordCount());
                    post.setCalculatedValue(postProcessingResult.calculatedValue());
                    LOGGER.info("updating fields wordCount and calculatedValue");
                    postRepository.save(post);
                }, () -> LOGGER.error("post with id {} not found", postId));
    }

    @Transactional
    public Page<PostSummaryOutput> search(final Pageable pageable) {
        return postRepository.findAll(pageable).map(this::summarize);
    }

    private PostOutput convert(final Post post) {
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
