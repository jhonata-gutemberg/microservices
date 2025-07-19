package dev.gutemberg.device.management.api.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SensorDetailOutput {
    private SensorOutput sensor;
    private SensorMonitoringOutput monitoring;
}
