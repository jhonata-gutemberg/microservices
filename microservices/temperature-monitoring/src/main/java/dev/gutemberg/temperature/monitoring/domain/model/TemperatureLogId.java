package dev.gutemberg.temperature.monitoring.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemperatureLogId {
    private UUID value;

    public TemperatureLogId(UUID value) {
        this.value = value;
    }

    public TemperatureLogId(String value) {
        this.value = UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
