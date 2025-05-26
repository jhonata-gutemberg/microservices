package dev.gutemberg.device.management.api.controller;

import dev.gutemberg.device.management.api.model.SensorInput;
import dev.gutemberg.device.management.common.IdGenerator;
import dev.gutemberg.device.management.domain.model.Sensor;
import dev.gutemberg.device.management.domain.model.SensorId;
import dev.gutemberg.device.management.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensors")
public class SensorController {
    private final SensorRepository sensorRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sensor create(@RequestBody SensorInput input) {
        final var sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generateTSID()))
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .enabled(false)
                .build();
        sensorRepository.save(sensor);
        return sensor;
    }
}
