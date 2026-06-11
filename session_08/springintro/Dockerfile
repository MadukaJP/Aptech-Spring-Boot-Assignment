# ============================================================
# STAGE 1: BUILD
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Fix Linux execute permission
RUN chmod +x mvnw

# Maven configuration
COPY pom.xml .

# Cache dependencies
RUN ./mvnw dependency:go-offline -B

# Application source
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# ============================================================
# STAGE 2: RUNTIME
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]