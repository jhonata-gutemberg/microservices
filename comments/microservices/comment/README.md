![Comment Service](docs/assets/comment-service.png)

Stores comments after checking with the Moderation service.

## 🚀 Run
- Prereqs: Java 21+
- Start Moderation first: `cd ../moderation && ./gradlew bootRun` (Windows: `gradlew.bat bootRun`)
- Then: `./gradlew bootRun`
- Port: `8080`

## 🔌 Endpoints
- POST `/api/comments`
  - Body: `{ "text": "Hello world", "author": "alice" }`
  - 201 Created: `{ id, text, author, createdAt }`
  - 422 Unprocessable Entity if moderation rejects (e.g., contains `hate`/`curse`).
  - 502/504 on upstream errors/timeouts to Moderation.
- GET `/api/comments/{id}` → One comment.
- GET `/api/comments?page=0&size=10` → Paged list.

## ⚙️ Configuration
- H2 (file) at `~/comment-db`.
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC: `jdbc:h2:file:~/comment-db;CASE_INSENSITIVE_IDENTIFIERS=TRUE;`
  - User: `sa`, Password: `sa`
- Moderation client base URL: `http://localhost:8081` (hardcoded in `api/client/ModerationRestClientFactory.java`).
- Timeouts: connect 3s, read 5s; 5xx from Moderation maps to 502 via `BadGatewayException`.

## 🧪 Examples
- Create (approved):
  - `curl -X POST http://localhost:8080/api/comments -H "Content-Type: application/json" -d '{"text":"Nice post!","author":"dev"}'`
- Create (rejected):
  - `curl -X POST http://localhost:8080/api/comments -H "Content-Type: application/json" -d '{"text":"I hate this","author":"dev"}'`
