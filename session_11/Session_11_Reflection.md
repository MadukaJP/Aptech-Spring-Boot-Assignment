# Session_11_Reflection

After configuring the Course Service to use Railway's private networking address:

```properties
student.service.url=${STUDENT_SERVICE_URL:http://localhost:8080}
```

with the Railway environment variable set to:

```text
http://student-service.railway.internal:8080
```

I removed the public domain from the Student Service and tested the `/course/verify-student/{studentId}` endpoint again.

The Course Service continued to work correctly and was still able to retrieve student information from the Student Service. This demonstrated that the public URL was not required for communication between the two backend services.

The reason is that the Course Service communicates with the Student Service through Railway's private network using the internal hostname `student-service.railway.internal`. Since both services are running within the same Railway environment, requests are routed internally without needing access through the public internet.

This experiment showed that backend services that only serve other backend services do not necessarily need public URLs. Using private networking is more secure because it reduces external exposure while still allowing service-to-service communication.

Therefore, in this setup, the Student Service does not need a public URL for the Course Service to function correctly, as long as private networking is configured properly.
