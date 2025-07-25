package dev.gutemberg.device.management.domain.repository;

import dev.gutemberg.device.management.domain.model.Sensor;
import dev.gutemberg.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
