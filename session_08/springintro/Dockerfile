# ============================================================
# SNIPPET 03: Dockerfile — Packaging Your App for the Cloud
# ============================================================
# A Dockerfile is a step-by-step recipe.
# Docker reads it and produces an "image" — a portable package
# containing your app + Java + everything needed to run.
#
# ANALOGY: A Dockerfile is like a recipe card.
# A Docker Image is the finished dish.
# A Docker Container is the dish being actively served.
#
# This uses a "Multi-Stage Build":
# Stage 1: Build the JAR (needs full JDK)
# Stage 2: Run the JAR  (only needs small JRE)
# Result: Final image is ~80% smaller than a single-stage build.
# ============================================================

# ─── STAGE 1: BUILD STAGE ───────────────────────────────────
# "eclipse-temurin:17-jdk-alpine" means:
# - eclipse-temurin = Adoptium OpenJDK (free, trusted)
# - 17 = Java 17 (matches our pom.xml)
# - jdk = Full development kit (we need this to compile)
# - alpine = Based on Alpine Linux (tiny, ~5MB base image)
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory inside the container
WORKDIR /app

# COPY STEP 1: Maven wrapper files first
# WHY: Docker caches layers. If pom.xml and source code haven't changed,
# Docker skips re-downloading dependencies. Faster builds!
COPY mvnw .
COPY .mvn .mvn

# COPY STEP 2: Just pom.xml
COPY pom.xml .

# Download all dependencies (this layer is cached unless pom.xml changes)
# -B = batch mode (no interactive prompts)
# go-offline = download everything needed without internet later
RUN ./mvnw dependency:go-offline -B

# COPY STEP 3: All source code
COPY src src

# Build the project and create the JAR file
# -DskipTests = don't run tests during Docker build (faster)
RUN ./mvnw package -DskipTests

# After this stage, the JAR is at: /app/target/spring-intro-0.0.1-SNAPSHOT.jar


# ─── STAGE 2: RUN STAGE ─────────────────────────────────────
# Use a SMALLER base image for running (JRE = Runtime only, not full JDK)
# eclipse-temurin:17-jre-alpine is ~100MB vs eclipse-temurin:17-jdk = ~400MB
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy ONLY the JAR from Stage 1 (not the entire Maven project!)
# target/*.jar matches the JAR regardless of its exact name
COPY --from=build /app/target/*.jar app.jar

# Declare which port the app uses
# (This is informational — Railway reads PORT environment variable)
EXPOSE 8080

# The command to run when the container starts
# ["java", "-jar", "app.jar"] is the Docker way of writing: java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─── WHAT HAPPENS WHEN RAILWAY RUNS THIS ────────────────────
# 1. Railway reads the Dockerfile
# 2. Pulls eclipse-temurin:17-jdk-alpine from Docker Hub
# 3. Runs each RUN command in order (or uses cached layers)
# 4. Creates the JAR in Stage 1
# 5. Pulls eclipse-temurin:17-jre-alpine for Stage 2
# 6. Copies the JAR to the smaller image
# 7. Starts the container with: java -jar app.jar
# 8. Injects PORT, DATABASE_URL, etc. as environment variables
# 9. Your app starts and Railway routes traffic to it