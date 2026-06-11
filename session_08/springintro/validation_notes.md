# Validation Notes

## 1. @NotNull vs @NotEmpty vs @NotBlank

Think of these as different levels of "make sure this isn't useless."

| Annotation | Rejects | Allows |
|---|---|---|
| **@NotNull** | `null` (nothing at all) | `""` (empty string), `" "` (spaces) |
| **@NotEmpty** | `null`, `""` (empty string) | `" "` (spaces) |
| **@NotBlank** | `null`, `""`, `" "` (spaces) | `"hello"` (actual text) |

**Example – a name field:**

```java
private String name;

// These values would pass @NotNull but fail @NotBlank:
// name = "";       // @NotNull ✅   @NotEmpty ❌   @NotBlank ❌
// name = "   ";    // @NotNull ✅   @NotEmpty ✅   @NotBlank ❌
// name = null;     // @NotNull ❌   @NotEmpty ❌   @NotBlank ❌
```

- **@NotNull** only cares that something exists (not `null`). An empty string is still a string object, so it passes.
- **@NotEmpty** wants at least one character. Spaces count as characters, so `" "` sneaks through.
- **@NotBlank** is the strictest — it needs real text. Spaces alone won't cut it.

In most real forms (name, email, etc.) you want **@NotBlank** because `"   "` isn't a valid name.

---

## 2. Why does BindingResult have to come immediately after @Valid?

Spring works like an assembly line:

```
@Valid Student student,   ← step 1: validate this object
BindingResult result      ← step 2: put the results here
```

If you put another parameter in between, Spring gets confused — it validates the object but has nowhere to put the errors before moving on to the next parameter. The `BindingResult` is like a basket that catches the error messages. No basket right after → errors get dropped → you can't check `result.hasErrors()` → bad data slips through.

It's a rule: **@Valid parameter first, BindingResult second, no exceptions.**

---

## 3. What does `th:object="${student}"` do and why does the form need it?

`th:object="${student}"` tells Thymeleaf: "Hey, every input field in this form belongs to the `student` object from the backend."

When you write:
```html
<input th:field="*{name}" />
```
The `*{name}` means "student.name" — it's a shortcut. Thymeleaf knows which object you're talking about because you set it with `th:object`.

**Why it's needed:**

- **Pre-filling:** When editing, the form automatically fills the inputs with the student's existing data (name, email, etc.) without you writing extra code.
- **Error recovery:** If validation fails, the form remembers what the user typed instead of going blank.
- **Cleaner code:** You write `*{name}` instead of `${student.name}` everywhere.

Without `th:object`, you'd have to manually wire every input to the model — a lot more work and easier to break.

---

## 4. What could a hacker do with NO server-side validation?

Client-side validation (JavaScript in the browser) is just a courtesy — it's trivially easy to bypass. A hacker can:

- Turn off JavaScript
- Use browser dev tools to remove `required` attributes
- Send raw HTTP requests with tools like Postman or cURL (completely bypassing the browser)
- Write a script that floods your server with thousands of requests

**Without server-side validation, they could:**

- Submit `null` or empty data → corrupt your database with missing fields
- Inject SQL (`'; DROP TABLE students; --`) → **delete your entire database**
- Store `<script>alert('hacked')</script>` in a name field → when an admin views the student list, the script runs and steals their session cookie (**XSS attack**)
- Submit a 10,000-character string where you expected 50 → crash your database or make pages unrenderable
- Submit fake email addresses or phone numbers → your school can never contact these students

**Server-side validation is your last line of defence.** The browser can lie; the server cannot trust anything it receives.

---

## 5. WAR file vs JAR file

| | JAR | WAR |
|---|---|---|
| **Stands for** | Java ARchive | Web ARchive |
| **What it holds** | Libraries, standalone apps, or reusable code | A complete web application (HTML, controllers, config) |
| **Needs a server?** | No — run it with `java -jar app.jar` (Spring Boot embeds its own server) | Yes — must be deployed to Tomcat, Jetty, WildFly, etc. |
| **Structure** | Just `.class` files and a manifest | Has a `WEB-INF/` folder with web-specific config (`web.xml`) |
| **Best for** | Spring Boot apps, microservices, utility libraries | Traditional Java web apps deployed to an external server |

**When to use each:**

- **JAR** — You're building a Spring Boot application with an embedded Tomcat server. You want to run it anywhere with just `java -jar`. This is what most modern projects use.
- **WAR** — Your company already has a managed Tomcat server, and you need to deploy multiple applications to it. Or you're maintaining an older project that follows the traditional approach.

These days, JAR-first is the default for new Spring Boot projects because it's simpler — one file, no server setup needed.
