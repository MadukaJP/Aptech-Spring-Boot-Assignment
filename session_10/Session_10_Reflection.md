# Session 10 Reflection

## Synchronous HTTP vs Asynchronous Message Queue (Kafka)

A synchronous HTTP call is like calling someone on the phone. You dial, you wait, and you cannot do anything else until they pick up and answer. If the other person is asleep (server down), you just sit there holding the phone, wasting time. That is what we did today with Feign. The Course Service calls the Student Service and waits for a response. If the Student Service is offline, the Course Service is stuck.

An asynchronous message queue like Kafka is like sending a text message. You write your message, send it, and immediately go back to whatever you were doing. The recipient reads it whenever they are ready. You do not wait. You do not care if they are asleep or busy. The message sits safely in the broker until they pick it up. If the Student Service is down for 3 days, the messages just queue up. When it comes back online, it processes them all.

## How would the Course Service know when a new student was created?

Instead of the Course Service asking "hey, any new students?" with an HTTP call, the Student Service would publish a "StudentCreated" event to a Kafka topic. The Course Service subscribes to that topic. Whenever a new student is created, Kafka automatically delivers the event to all subscribers. No HTTP call needed. No waiting. The Course Service simply listens and reacts when new data arrives. It is like subscribing to a YouTube channel instead of refreshing the homepage every 10 seconds hoping to see something new.
