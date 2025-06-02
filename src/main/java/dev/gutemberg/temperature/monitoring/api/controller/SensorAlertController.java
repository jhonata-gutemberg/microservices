package dev.gutemberg.temperature.monitoring.api.controller;

import dev.gutemberg.temperature.monitoring.api.model.SensorAlertInput;
import dev.gutemberg.temperature.monitoring.api.model.SensorAlertOutput;
import dev.gutemberg.temperature.monitoring.common.IdGenerator;
import dev.gutemberg.temperature.monitoring.domain.model.SensorAlert;
import dev.gutemberg.temperature.monitoring.domain.model.SensorId;
import dev.gutemberg.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {
    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getOne(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToModel(sensorAlert);
    }

    @PutMapping
    public SensorAlertOutput upsert(@PathVariable TSID sensorId, @RequestBody SensorAlertInput sensorAlertInput) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(new SensorAlert());
        sensorAlert.setId(new SensorId(sensorId));
        sensorAlert.setMaxTemperature(sensorAlertInput.getMaxTemperature());
        sensorAlert.setMinTemperature(sensorAlertInput.getMinTemperature());
        sensorAlert = sensorAlertRepository.saveAndFlush(sensorAlert);
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
