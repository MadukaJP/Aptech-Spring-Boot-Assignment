# WebFlux - Reactive News Service

A Spring WebFlux demo that streams live news alerts using reactive programming and Server-Sent Events (SSE).

## Endpoints

| Endpoint | What it does |
|---|---|
| `GET /api/news` | Returns 3 news items all at once |
| `GET /api/news/stream` | Returns 3 news items, one every 2 seconds |
| `GET /api/news/live` | Infinite stream of random news alerts, one per second |

## Run it

```bash
./mvnw spring-boot:run
```

The server starts on **port 4000**.

Open your browser to `http://localhost:4000/api/news/live` and watch news alerts pour in every second.

## Built with

- Spring Boot 4.0.6
- Spring WebFlux + Project Reactor
- Java 17
- Netty (non-blocking HTTP server)
