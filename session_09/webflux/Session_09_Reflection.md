# Session 09 Reflection  -  Blocking vs Reactive

## The Blocking Question

Imagine a restaurant with only one waiter. If a customer sits down and the waiter stands right next to them, waiting for them to order, eat, pay, and leave  -  that waiter can't serve anyone else. Now imagine 100 customers walk in at the same time. You'd need 100 waiters (one per customer), but the restaurant only has 20. The other 80 customers just wait forever.

That's what happens with Spring MVC. Each user visiting `/api/news/live` gets their own dedicated server thread that does nothing but wait for the next news item, forever, since the stream never ends. After 100 users, the server runs out of threads. New visitors get nothing  -  the server is effectively frozen.

## The Reactive Answer

Now picture a barista at a coffee shop. Customers place their order, get a buzzer, and sit down. The barista doesn't stand around waiting for each person. They make coffee after coffee, buzzing customers only when their drink is ready. One barista can handle dozens of people at once.

That's Spring WebFlux with Netty. Instead of giving each user a dedicated thread, a tiny team of event-loop threads juggles all 100 connections at the same time. No thread sits around waiting. When it's time to send the next news alert to a user, the event loop just... sends it. All 100 users get their news, and the server barely breaks a sweat  -  using only a handful of threads the whole time.
