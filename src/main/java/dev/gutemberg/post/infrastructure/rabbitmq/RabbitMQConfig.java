package dev.gutemberg.post.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String POST_PROCESSING_EXCHANGE = "post-service.post-processing.v1.e";
    public static final String POST_PROCESSING_RESULT_QUEUE = "post-service.post-processing-result.v1.q";
    private static final String POST_PROCESSING_RESULT_EXCHANGE = "text-processor-service.post-processing-result.v1.e";

    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(final ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange postProcessingExchange() {
        return ExchangeBuilder.fanoutExchange(POST_PROCESSING_EXCHANGE).build();
    }

    @Bean
    public Binding postProcessingResultBinding() {
        return BindingBuilder.bind(postProcessingResultQueue()).to(postProcessingResultExchange());
    }

    @Bean
    public Queue postProcessingResultQueue() {
        return QueueBuilder.durable(POST_PROCESSING_RESULT_QUEUE).build();
    }

    public FanoutExchange postProcessingResultExchange() {
        return ExchangeBuilder.fanoutExchange(POST_PROCESSING_RESULT_EXCHANGE).build();
    }
}
