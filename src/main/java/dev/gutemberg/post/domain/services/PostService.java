package dev.gutemberg.post.domain.services;

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
import java.util.Optional;
import java.util.UUID;

import static dev.gutemberg.post.infrastructure.rabbitmq.RabbitMQConfig.POST_PROCESSING_EXCHANGE;

@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    public PostService(final PostRepository postRepository, final RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Post create(Post post) {
        final var message = new PostProcessingRequest(post.getId(), post.getBody());
        rabbitTemplate.convertAndSend(POST_PROCESSING_EXCHANGE, "", message);
        return postRepository.saveAndFlush(post);
    }

    @Transactional
    public Optional<Post> getOne(final UUID postId) {
        return postRepository.findById(postId);
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
    public Page<Post> search(final Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
