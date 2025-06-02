package dev.gutemberg.temperature.monitoring.api.controller;

import dev.gutemberg.temperature.monitoring.api.model.SensorAlertOutput;
import dev.gutemberg.temperature.monitoring.domain.model.SensorAlert;
import dev.gutemberg.temperature.monitoring.domain.model.SensorId;
import dev.gutemberg.temperature.monitoring.domain.repository.SensorAlertRepository;
import dev.gutemberg.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {
    private SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getOne(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToModel(sensorAlert);
    }

    private SensorAlertOutput convertToModel(SensorAlert sensorAlert) {
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }
}
