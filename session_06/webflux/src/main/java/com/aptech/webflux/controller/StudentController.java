package com.aptech.webflux.controller;

import com.aptech.webflux.model.Student;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    // In-memory data (no database for this session)
    private static final List<Student> students = List.of(
        new Student(1, "Alice Johnson",  "Spring MVC",      "A"),
        new Student(2, "Bob Martins",    "Spring Boot",     "B"),
        new Student(3, "Charlie Okafor","Spring Security",  "A"),
        new Student(4, "Diana Adeyemi", "WebFlux",          "B"),
        new Student(5, "Emeka Chukwu",  "Microservices",    "C")
    );


    // ════════════════════════════════════════════════════════
    // BASIC CRUD ENDPOINTS
    // ════════════════════════════════════════════════════════

    // GET /students → All students as JSON array
    // Returns Flux<Student> → Spring serializes each as JSON automatically
    @GetMapping
    public Flux<Student> getAllStudents() {
        return Flux.fromIterable(students);
    }

    // GET /students/{id} → One student (404 if not found)
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable int id) {
        return Mono.justOrEmpty(
            students.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst()
        )
        .map(s -> ResponseEntity.ok(s))              // found → 200 OK + student data
        .defaultIfEmpty(ResponseEntity.notFound().build()); // not found → 404 Not Found
    }


    // ════════════════════════════════════════════════════════
    // REACTOR OPERATOR DEMOS
    // ════════════════════════════════════════════════════════

    // GET /students/grade/A  → Students with exactly grade "A"
    @GetMapping("/grade/{grade}")
    public Flux<Student> getByGrade(@PathVariable String grade) {
        return Flux.fromIterable(students)
                   .filter(s -> s.getGrade().equalsIgnoreCase(grade));
        //  .filter() — keeps only items where the condition is TRUE
    }

    // GET /students/names → Just the names, sorted alphabetically
    @GetMapping("/names")
    public Flux<String> getAllNames() {
        return Flux.fromIterable(students)
                   .map(s -> s.getName())           // Student → String (just the name)
                   .sort(Comparator.naturalOrder()); // sort alphabetically
    }

    // GET /students/count → How many students total
    @GetMapping("/count")
    public Mono<Long> countStudents() {
        return Flux.fromIterable(students)
                   .count(); // reduces Flux into a single Mono<Long>
    }

    // GET /students/top/2 → First 2 students only
    @GetMapping("/top/{n}")
    public Flux<Student> getTopN(@PathVariable int n) {
        return Flux.fromIterable(students)
                   .take(n); // take(n) — emits at most n items, then completes
    }


    // ════════════════════════════════════════════════════════
    // LIVE STREAMING ENDPOINT (The WebFlux Superpower)
    // ════════════════════════════════════════════════════════

    // GET /students/live-feed
    // produces = "text/event-stream" → Server-Sent Events format
    // The browser stays connected and receives data pushed from the server.
    // Each student arrives one by one, 2 seconds apart.
    // This is IMPOSSIBLE with traditional Spring MVC.
    @GetMapping(value = "/live-feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> liveFeed() {
        return Flux.fromIterable(students)
                   .delayElements(Duration.ofSeconds(2)) // emit one student every 2 seconds
                   .log(); // logs each reactive event to IntelliJ console
    }

    // GET /students/ticker → Live name ticker, one name every second
    @GetMapping(value = "/ticker", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> nameTicker() {
        return Flux.fromIterable(students)
                   .map(s -> "🎓 " + s.getName() + " — " + s.getCourse())
                   .delayElements(Duration.ofSeconds(1));
    }


}