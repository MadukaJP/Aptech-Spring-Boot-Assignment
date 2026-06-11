package com.aptech.webflux.controller;

import com.aptech.webflux.model.Course;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final List<Course> courses = List.of(
        new Course(1, "Spring Boot",         "John Doe",     40, "Intermediate"),
        new Course(2, "Java Fundamentals",    "Jane Smith",   30, "Beginner"),
        new Course(3, "Advanced React",       "Bob Johnson",  45, "Advanced"),
        new Course(4, "Python for Data Science", "Alice Williams", 35, "Intermediate"),
        new Course(5, "Web Development",      "Charlie Brown", 25, "Beginner")
    );

    @GetMapping
    public Flux<Course> getAllCourses() {
        return Flux.fromIterable(courses)
                   .sort(Comparator.comparing(Course::getTitle));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Course>> getCourseById(@PathVariable int id) {
        return Mono.justOrEmpty(
            courses.stream()
                   .filter(c -> c.getId() == id)
                   .findFirst()
        )
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/level/{level}")
    public Flux<Course> getByLevel(@PathVariable String level) {
        return Flux.fromIterable(courses)
                   .filter(c -> c.getLevel().equalsIgnoreCase(level));
    }

    @GetMapping("/count")
    public Mono<Long> countCourses() {
        return Flux.fromIterable(courses)
                   .count();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Course> streamCourses() {
        return Flux.fromIterable(courses)
                   .delayElements(Duration.ofSeconds(1))
                   .log();
    }
}
