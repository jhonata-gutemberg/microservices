package dev.gutemberg.temperature.monitoring.infrastructure.rabbitmq;

import dev.gutemberg.temperature.monitoring.api.model.TemperatureLogData;
import dev.gutemberg.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import java.time.Duration;

import static dev.gutemberg.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {
    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }
}
