package dev.gutemberg.device.management.api.client;

import dev.gutemberg.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/sensors/{sensorId}/monitoring")
public interface SensorMonitoringClient {
    @PutExchange("/enable")
    void enableMonitoring(@PathVariable TSID sensorId);

    @DeleteExchange("/enable")
    void disableMonitoring(@PathVariable TSID sensorId);

    @GetExchange
    SensorMonitoringOutput getDetail(@PathVariable TSID sensorId);
}
