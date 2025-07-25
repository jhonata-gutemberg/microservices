package dev.gutemberg.temperature.monitoring.domain.repository;

import dev.gutemberg.temperature.monitoring.domain.model.SensorAlert;
import dev.gutemberg.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
