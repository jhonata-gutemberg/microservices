package dev.gutemberg.text.processor.domain.services;

import dev.gutemberg.text.processor.domain.models.PostInput;
import dev.gutemberg.text.processor.domain.models.PostOutput;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static dev.gutemberg.text.processor.infraestructure.rabbitmq.RabbitMQConfig.POST_PROCESSING_RESULT_EXCHANGE;

@Service
public class PostService {
    private static final double PRICE_PER_WORD_IN_CENTS = 0.1;

    private final RabbitTemplate rabbitTemplate;

    public PostService(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void process(final PostInput postInput) {
        final int wordCount = postInput.postBody().length();
        final double calculatedValue = wordCount * PRICE_PER_WORD_IN_CENTS;
        final var postOutput = new PostOutput(postInput.postId(), wordCount, calculatedValue);
        rabbitTemplate.convertAndSend(POST_PROCESSING_RESULT_EXCHANGE, "", postOutput);
    }
}
