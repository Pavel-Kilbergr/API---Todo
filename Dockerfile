# Simple single-stage build for Render
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy everything
COPY . .

# Build in one step
RUN chmod +x gradlew && ./gradlew build -x test --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/todo-api-0.0.1-SNAPSHOT.jar"]
