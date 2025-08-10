# Multi-stage build for Spring Boot application
FROM openjdk:17-jdk-slim AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle/

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src src/

# Build the application
RUN ./gradlew build -x test --no-daemon

# Production stage
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/todo-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
