package dev.gutemberg.text.processor.infraestructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String POST_PROCESSING_QUEUE = "text-processor-service.post-processing.v1.q";
    private static final String POST_PROCESSING_EXCHANGE = "post-service.post-processing.v1.e";
    public static final String POST_PROCESSING_RESULT_EXCHANGE = "text-processor-service.post-processing-result.v1.e";

    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue postProcessingQueue() {
        return QueueBuilder.durable(POST_PROCESSING_QUEUE).build();
    }

    @Bean
    public Binding postProcessingBinding() {
        return BindingBuilder.bind(postProcessingQueue()).to(postProcessingExchange());
    }

    public FanoutExchange postProcessingExchange() {
        return ExchangeBuilder.fanoutExchange(POST_PROCESSING_EXCHANGE).build();
    }

    @Bean
    public FanoutExchange postProcessingResultExchange() {
        return ExchangeBuilder.fanoutExchange(POST_PROCESSING_RESULT_EXCHANGE).build();
    }
}
