package dev.gutemberg.text.processor.infraestructure.rabbitmq;

import dev.gutemberg.text.processor.domain.models.PostInput;
import dev.gutemberg.text.processor.domain.services.PostService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import static dev.gutemberg.text.processor.infraestructure.rabbitmq.RabbitMQConfig.*;

@Configuration
public class RabbitMQListener {
    private final PostService postService;

    public RabbitMQListener(final PostService postService) {
        this.postService = postService;
    }

    @RabbitListener(queues = POST_PROCESSING_QUEUE)
    public void handlePostProcessing(@Payload final PostInput postInput) {
        postService.process(postInput);
    }
}
