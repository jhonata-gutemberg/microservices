![Temperature Processing](docs/assets/temperature-processing.png)

Spring Boot service that ingests plain-text temperature readings and publishes validated events to RabbitMQ.

## 🚪 Port
- Runs on `8081` (see `src/main/resources/application.yml`).

## 🚀 Run
1) Ensure RabbitMQ is running: `docker compose up -d` (root folder)
2) Start the service:
   - Unix/macOS: `./gradlew bootRun`
   - Windows: `gradlew.bat bootRun`

## 📡 Endpoint
- `POST /api/sensors/{sensorId}/temperatures/data`
  - `Content-Type: text/plain`
  - Body: a numeric temperature value (e.g., `22.7`)
  - Responses: `204` on success; `400` for blank or invalid values

Example:
```bash
curl -X POST -H "Content-Type: text/plain" \
  --data "22.7" \
  http://localhost:8081/api/sensors/{sensorId}/temperatures/data
```

## 🐇 Messaging
- Publishes to RabbitMQ (host `localhost`, port `5672`, user/pass `rabbitmq`/`rabbitmq`).
