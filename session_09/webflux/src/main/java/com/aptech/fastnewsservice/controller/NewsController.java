package com.aptech.fastnewsservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

@RestController
public class NewsController {

    private final Random random = new Random();

    // ────────────────────────────────────────────────────────────
    // 1. Basic Flux (Instantly returns all items)
    // ────────────────────────────────────────────────────────────
    @GetMapping("/api/news")
    public Flux<String> getBreakingNews() {
        return Flux.just(
                "News 1: Server successfully started",
                "News 2: Database connection established",
                "News 3: Application is healthy"
        );
    }

    // ────────────────────────────────────────────────────────────
    // 2. Delayed Event Stream (Proves non-blocking behavior)
    // ────────────────────────────────────────────────────────────
    // The MediaType.TEXT_EVENT_STREAM_VALUE is critical.
    // Without it, the browser waits 6 seconds for the entire Flux
    // to finish before showing anything.
    // WITH it, the browser shows each string immediately as it arrives!
    
    @GetMapping(value = "/api/news/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamBreakingNews() {
        return Flux.just(
                "🔴 BREAKING: Market hits all-time high!",
                "🟡 UPDATE: New framework released for Java.",
                "🟢 TECH: AI writes code faster than humans."
        )
        .delayElements(Duration.ofSeconds(2)); // Emit one item every 2 seconds
    }



    @GetMapping(value = "/api/news/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getLiveInfiniteNews() {

        String[] companies = {
                "Apple",
                "Google",
                "Tesla",
                "Microsoft"
        };

        String[] events = {
                "stock drops 5%",
                "announces new product",
                "hires new CEO",
                "reports record profits"
        };

        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
                    String company = companies[random.nextInt(companies.length)];
                    String event = events[random.nextInt(events.length)];

                    return "Alert " + (tick + 1) + ": " + company + " " + event;
                });
    }
}