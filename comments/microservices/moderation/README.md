![Moderation Service](docs/assets/moderation-service.png)

Approves or rejects comment text based on a simple forbidden-words list.

## 🚀 Run
- Prereqs: Java 21+
- Start: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)
- Port: `8081`

## 🔌 Endpoint
- POST `/api/moderate`
  - Body: `{ "text": "...", "commentId": "<uuid>" }`
  - Response: `{ "approved": true|false, "reason": "..." }`
- Forbidden words (default): `hate`, `curse` (see `api/controller/ModerationController.java`).

## 🧩 Notes
- Stateless service used by the Comment service before persisting data.
- Adjust the moderation rules in `ModerationController` as needed.
