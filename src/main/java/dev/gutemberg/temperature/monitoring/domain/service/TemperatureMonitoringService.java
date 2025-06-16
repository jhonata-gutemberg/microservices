package dev.gutemberg.temperature.monitoring.domain.service;

import dev.gutemberg.temperature.monitoring.api.model.TemperatureLogData;
import dev.gutemberg.temperature.monitoring.domain.model.SensorId;
import dev.gutemberg.temperature.monitoring.domain.model.SensorMonitoring;
import dev.gutemberg.temperature.monitoring.domain.model.TemperatureLog;
import dev.gutemberg.temperature.monitoring.domain.model.TemperatureLogId;
import dev.gutemberg.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import dev.gutemberg.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureMonitoringService {
    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(
                        sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData)
                );
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);
            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .build();
            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature Updated: SensorId {} Temp {}",
                    temperatureLogData.getSensorId(),
                    temperatureLogData.getValue());
            return;
        }
        logIgnoredTemperature(temperatureLogData);
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature Ignored: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }
}
