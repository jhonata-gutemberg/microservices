package dev.gutemberg.temperature.monitoring.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorMonitoring {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "sensorId", columnDefinition = "BIGINT"))
    private SensorId id;
    private Double lastTemperature;
    private Boolean enabled;
    private OffsetDateTime updatedAt;

    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}
