# Railway Restart — Cloud Resilience Test

| Metric          | Value                        |
|-----------------|------------------------------|
| Restart duration | 50–60 seconds                |
| HTTP status observed | **502 Bad Gateway**     |
| Recovery         | Automatic (no manual intervention) |

## Observation

During the restart, the app returned a **502 Bad Gateway** error while Railway recycled the container. After approximately 50–60 seconds, the app came back online automatically without any manual intervention. This demonstrates cloud platform resilience — the platform handles container restarts, health checks, and recovery transparently.
