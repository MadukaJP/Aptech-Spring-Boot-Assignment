# Concept Questions

## 1. Filter + Take

You have 50 courses. `.filter()` lets 5 through. Then `.take(3)` says "I only want 3". **Final subscriber gets 3 items.** Even though 5 passed the filter, `.take(3)` cuts it short.

---

## 2. Why not RestTemplate in WebFlux?

WebFlux is a chef juggling 20 orders at once. RestTemplate is handing that chef a phone and asking him to wait on hold. He stops everything. **Use WebClient instead** — it's non-blocking and lets the chef keep cooking.

---

## 3. Blocking JDBC in a WebFlux controller?

You're at a fast-food drive-through. The cashier stops everything to go count inventory in the back. The whole line stalls. That's what a blocking JDBC call does — it grabs a thread and won't let go. Other requests pile up. Your "reactive" app gets slower than a regular MVC one. **Bad news:** no error, just silent performance death.
