![Device Management](docs/assets/device-management.png)

Spring Boot service for managing sensors (CRUD) and toggling monitoring. Exposes REST APIs and persists data using H2.

## 🚪 Port
- Runs on `8080` (see `src/main/resources/application.yml`).

## 🚀 Run
- RabbitMQ optional for this service, but recommended for full system.
- Unix/macOS: `./gradlew bootRun`
- Windows: `gradlew.bat bootRun`

H2 Console: `http://localhost:8080/h2-console` (user: `sa`, pass: `123`).

## 📡 Endpoints
- `GET /api/sensors` — List sensors (paged)
- `GET /api/sensors/{sensorId}` — Get one sensor
- `GET /api/sensors/{sensorId}/detail` — Sensor data + monitoring snapshot
- `POST /api/sensors` — Create sensor
- `PUT /api/sensors/{sensorId}` — Update sensor
- `DELETE /api/sensors/{sensorId}` — Delete sensor
- `PUT /api/sensors/{sensorId}/enable` — Enable monitoring
- `DELETE /api/sensors/{sensorId}/enable` — Disable monitoring

### Create/Update Body
```json
{
  "name": "Sensor A",
  "ip": "10.0.0.5",
  "location": "Lab",
  "protocol": "HTTP",
  "model": "X100"
}
```

## 🗃️ Data
- H2 file: `~/sensors-device-management-db`
- JPA DDL auto: `update`

## 🔗 Related
- Calls Temperature Monitoring for detail and enabling/disabling monitoring.
