package dev.gutemberg.post.infrastructure.rabbitmq;

import dev.gutemberg.post.domain.models.PostProcessingResult;
import dev.gutemberg.post.domain.services.PostService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class RabbitMQListener {
    private final PostService postService;

    public RabbitMQListener(final PostService postService) {
        this.postService = postService;
    }

    @RabbitListener(queues = RabbitMQConfig.POST_PROCESSING_RESULT_QUEUE)
    public void handlePostProcessingResult(@Payload final PostProcessingResult postProcessingResult) {
        postService.processResult(postProcessingResult);
    }
}
