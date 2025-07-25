package dev.gutemberg.temperature.monitoring.domain.service;

import dev.gutemberg.temperature.monitoring.api.model.TemperatureLogData;
import dev.gutemberg.temperature.monitoring.domain.model.SensorId;
import dev.gutemberg.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {
    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert ->
                        {
                            if (alert.getMaxTemperature() != null
                                    && temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                                log.info("Alert Max Temp: SensorId {} Temp {}",
                                        temperatureLogData.getSensorId(),
                                        temperatureLogData.getValue());
                                return;
                            }
                            if (alert.getMinTemperature() != null
                                    && temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                                log.info("Alert Min Temp: SensorId {} Temp {}",
                                        temperatureLogData.getSensorId(),
                                        temperatureLogData.getValue());
                                return;
                            }
                            logIgnoredAlert(temperatureLogData);
                        }, () -> logIgnoredAlert(temperatureLogData)
                );
    }

    private static void logIgnoredAlert(TemperatureLogData temperatureLogData) {
        log.info("Alert Ignored: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }
}
