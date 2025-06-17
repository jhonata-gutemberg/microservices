package dev.gutemberg.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PROCESS_TEMPERATURE_QUEUE = "temperature-monitoring.process-temperature.v1.q";
    public static final String ALERTING_QUEUE = "temperature-monitoring.alerting.v1.q";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue processTemperatureQueue() {
        return QueueBuilder.durable(PROCESS_TEMPERATURE_QUEUE).build();
    }

    @Bean
    public Queue alertingQueue() {
        return QueueBuilder.durable(ALERTING_QUEUE).build();
    }

    @Bean
    public Binding processTemperatureBinding() {
        return BindingBuilder.bind(processTemperatureQueue()).to(exchange());
    }

    @Bean
    public Binding alertingBinding() {
        return BindingBuilder.bind(alertingQueue()).to(exchange());
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    private FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }
}
