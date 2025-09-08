![Comments](docs/assets/comments.png)

A tiny two-service Spring Boot system demonstrating inter-service communication and basic content moderation.

## 🧭 Overview
- **Services:** `comment` (stores comments) and `moderation` (approves/rejects text).
- **Flow:** The `comment` service calls `moderation` before saving. If rejected, the comment is not stored and a 422 is returned.
- **Ports:** `comment` on `8080`, `moderation` on `8081`.
- **Storage:** `comment` uses H2 (file DB at `~/comment-db`).

## 🚀 Quick Start
- **Prereqs:** Java 21+. Gradle wrapper is included.
- **Run moderation:** `cd moderation && ./gradlew bootRun` (Windows: `gradlew.bat bootRun`).
- **Run comment:** `cd comment && ./gradlew bootRun` (after moderation is up).

## 🧪 Try It
- Create an approved comment:
  - `curl -X POST http://localhost:8080/api/comments -H "Content-Type: application/json" -d '{"text":"Hello world","author":"alice"}'`
- Create a rejected comment (contains forbidden word):
  - `curl -X POST http://localhost:8080/api/comments -H "Content-Type: application/json" -d '{"text":"I feel hate","author":"bob"}'`
- Get one by id:
  - `curl http://localhost:8080/api/comments/<uuid>`
- List (paged):
  - `curl "http://localhost:8080/api/comments?page=0&size=10"`

## 🔌 APIs
- `comment`:
  - `POST /api/comments` → 201 Created with `{id,text,author,createdAt}` or `422` if not approved.
  - `GET /api/comments/{id}` → Returns a single comment.
  - `GET /api/comments` → Spring Data page of comments.
- `moderation`:
  - `POST /api/moderate` with `{text, commentId}` → `{approved, reason}`.

## ⚙️ Configuration
- `comment`:
  - H2 console enabled at `http://localhost:8080/h2-console`.
  - JDBC: `jdbc:h2:file:~/comment-db;CASE_INSENSITIVE_IDENTIFIERS=TRUE;`, user `sa`, password `sa`.
  - Moderation base URL: `http://localhost:8081` (see `comment/src/main/java/.../ModerationRestClientFactory.java`).
- `moderation`:
  - Forbidden words: `hate`, `curse` (see `ModerationController`).

## 🗂️ Structure
- `comment/` — REST API for creating and reading comments (JPA + H2).
- `moderation/` — Simple text moderation service.

## 🧱 Tech Stack
- Java 21, Spring Boot 3, Spring Web, Spring Data JPA, H2, Gradle.

## 📝 Notes
- Start `moderation` before `comment` to avoid `502/504` gateway errors from the client fallback.
