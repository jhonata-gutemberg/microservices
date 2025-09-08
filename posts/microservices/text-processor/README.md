![Text Processor Service](docs/assets/text-processor-service.png)

Small Spring Boot service that consumes post-processing requests from RabbitMQ, performs a simple text analysis, and publishes the processing result to a fanout exchange.

## 🚀 Overview
- Listens for `PostInput` messages on a durable queue.
- Computes simple metrics from `postBody` and a price estimate.
- Publishes a `PostOutput` message with the results.

## 🔄 How It Works
1. Producer publishes to fanout exchange: `post-service.post-processing.v1.e`.
2. This service consumes from queue: `text-processor-service.post-processing.v1.q`.
3. Service calculates metrics:
   - count = `postBody.length()` (number of characters)
   - price = `count * 0.1` (cents per character)
4. Emits `PostOutput` to fanout exchange: `text-processor-service.post-processing-result.v1.e`.

Note: The output field is named `wordCount` but currently reflects character count based on implementation.

## 📨 Message Contracts
- Input (`PostInput`):
  - `postId` (UUID)
  - `postBody` (string)

- Output (`PostOutput`):
  - `postId` (UUID)
  - `wordCount` (long) — character count per current logic
  - `calculatedValue` (double) — price in cents

Example input JSON:
```json
{
  "postId": "f1a2b3c4-d5e6-4711-8899-aabbccddeeff",
  "postBody": "Hello world!"
}
```

Example output JSON:
```json
{
  "postId": "f1a2b3c4-d5e6-4711-8899-aabbccddeeff",
  "wordCount": 12,
  "calculatedValue": 1.2
}
```

## 🔧 Configuration
- App name: `text-processor`
- Server port: `8081`
- RabbitMQ (defaults in `src/main/resources/application.yml`):
  - `spring.rabbitmq.host=localhost`
  - `spring.rabbitmq.port=5672`
  - `spring.rabbitmq.username=rabbitmq`
  - `spring.rabbitmq.password=rabbitmq`

Exchanges and queues (from `RabbitMQConfig`):
- Inbound exchange (fanout): `post-service.post-processing.v1.e`
- Inbound queue: `text-processor-service.post-processing.v1.q`
- DLQ: `text-processor-service.post-processing.v1.dlq`
- Outbound exchange (fanout): `text-processor-service.post-processing-result.v1.e`

## 🧪 Quick Test via RabbitMQ
Publish a test message to the inbound exchange using `rabbitmqadmin` (adjust `postId`):
```bash
rabbitmqadmin publish \
  exchange=post-service.post-processing.v1.e \
  routing_key='' \
  properties='{"content_type":"application/json"}' \
  payload='{"postId":"f1a2b3c4-d5e6-4711-8899-aabbccddeeff","postBody":"Hello world!"}'
```

Subscribe a temporary queue to the outbound exchange to observe results, or bind an existing consumer to `text-processor-service.post-processing-result.v1.e`.

## 🐞 Troubleshooting
- Ensure RabbitMQ is running and credentials match `application.yml`.
- Verify that the inbound queue and exchanges are created (the app declares them on startup).
- If messages are not consumed, check the DLQ: `text-processor-service.post-processing.v1.dlq`.

## 📁 Project Structure
- `domain/models`: message records (`PostInput`, `PostOutput`).
- `domain/services`: processing logic (`PostService`).
- `infraestructure/rabbitmq`: AMQP config, listener, and declaration.
- `application.yml`: service and RabbitMQ configuration.

## 📌 Notes
- Pricing constant: `0.1` (cents per character). Update in `PostService` if pricing model changes.
