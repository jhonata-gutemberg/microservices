![Posts Microservices](docs/assets/posts.png)

Simple microservices demo composed of two Spring Boot apps that communicate via RabbitMQ:

- post: REST API + Vaadin UI to create and view posts. Persists to H2, publishes processing requests, and consumes processing results.
- text-processor: Background worker that consumes post processing requests, computes metrics, and publishes results back.

RabbitMQ is provided via docker-compose. This repo is meant for local experimentation and learning.

## 🏗️ Architecture
- Broker: RabbitMQ (`docker-compose.yml`)
- Service `post` (Java 21, Spring Boot 3, Vaadin 24, H2)
  - REST endpoints under `/api/posts`
  - Vaadin UI at `/` (port 8080)
  - Publishes to exchange `post-service.post-processing.v1.e`
  - Listens to queue `post-service.post-processing-result.v1.q`
- Service `text-processor` (Java 21, Spring Boot 3)
  - Listens to queue `text-processor-service.post-processing.v1.q`
  - Publishes to exchange `text-processor-service.post-processing-result.v1.e`

Message flow:
1) post -> fanout exchange `post-service.post-processing.v1.e` -> queue `text-processor-service.post-processing.v1.q`
2) text-processor computes metrics and publishes to `text-processor-service.post-processing-result.v1.e`
3) post is bound to that exchange and updates the stored post with the result

Notes on “word count”: for simplicity, the worker currently uses the post body length (characters) as the value exposed as `wordCount`, and calculates `calculatedValue = length * 0.1` (cents).

## 🧰 Prerequisites
- Java 21
- Docker Desktop (or Docker Engine) for RabbitMQ
- Bash/CMD to run Gradle wrappers

## 🚀 Run
1) Start RabbitMQ
   - From repo root: `docker compose up -d`
   - Management UI: http://localhost:15672 (user `rabbitmq`, pass `rabbitmq`)
2) Start services (in separate terminals)
   - post: `cd post && ./gradlew bootRun`
   - text-processor: `cd text-processor && ./gradlew bootRun`

Ports:
- post: `http://localhost:8080` (UI) and `http://localhost:8080/api` (REST)
- text-processor: `http://localhost:8081`
- RabbitMQ: `amqp://localhost:5672`, management at `http://localhost:15672`

H2 Console (post service):
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:~/posts-db;CASE_INSENSITIVE_IDENTIFIERS=TRUE`
- User: `sa`, Password: `123`

## 📬 Messaging details
- Exchanges
  - Requests: `post-service.post-processing.v1.e` (fanout)
  - Results: `text-processor-service.post-processing-result.v1.e` (fanout)
- Queues
  - Worker input: `text-processor-service.post-processing.v1.q` (DLQ: `text-processor-service.post-processing.v1.dlq`)
  - Post results: `post-service.post-processing-result.v1.q` (DLQ: `post-service.post-processing-result.v1.dlq`)
- Payloads
  - Request: `{ postId: UUID, postBody: string }`
  - Result: `{ postId: UUID, wordCount: number, calculatedValue: number }`

## 🛠️ Build
- post: `cd post && ./gradlew build`
- text-processor: `cd text-processor && ./gradlew build`

## 🧯 Troubleshooting
- RabbitMQ connection errors: ensure `docker compose up -d` is running and credentials match in `application.yml`.
- H2 database reset: stop the `post` app and remove the file `~/posts-db.*` (data loss!).
- UI doesn’t show metrics: give it a moment after creating a post; the async worker must process and send the result.

## 🗂️ Project layout
- `docker-compose.yml`: RabbitMQ with persistence volume `posts-rabbitmq`
- `post/`: Spring Boot + Vaadin app (REST, UI, H2, AMQP)
- `text-processor/`: Spring Boot worker (AMQP)
