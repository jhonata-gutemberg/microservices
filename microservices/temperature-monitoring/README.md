![Temperature Monitoring](docs/assets/temperature-monitoring.png)

Spring Boot service that consumes temperature events from RabbitMQ, stores logs, manages monitoring state, and alert thresholds. Exposes REST APIs for querying and control.

## 🚪 Port
- Runs on `8082` (see `src/main/resources/application.yml`).

## 🚀 Run
1) Ensure RabbitMQ is running: `docker compose up -d` (root folder)
2) Start the service:
   - Unix/macOS: `./gradlew bootRun`
   - Windows: `gradlew.bat bootRun`

## 📡 Endpoints
- Logs
  - `GET /api/sensors/{sensorId}/temperatures` — Paged temperature logs
- Monitoring
  - `GET /api/sensors/{sensorId}/monitoring` — Current state
  - `PUT /api/sensors/{sensorId}/monitoring/enable` — Enable monitoring
  - `DELETE /api/sensors/{sensorId}/monitoring/enable` — Disable monitoring
- Alerts
  - `GET /api/sensors/{sensorId}/alert` — Get thresholds
  - `PUT /api/sensors/{sensorId}/alert` — Upsert thresholds
  - `DELETE /api/sensors/{sensorId}/alert` — Remove thresholds

### Alert Upsert Body
```json
{
  "maxTemperature": 30.0,
  "minTemperature": 10.0
}
```

## 🗃️ Data
- H2 file: `~/sensors-temperature-monitoring-db`
- JPA DDL auto: `update`

## 🐇 Messaging
- RabbitMQ consumer (host `localhost`, port `5672`, user/pass `rabbitmq`/`rabbitmq`).
