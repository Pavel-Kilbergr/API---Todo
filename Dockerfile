FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy files explicitly
COPY gradlew ./
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY src/ src/

# Debug: check if gradle-wrapper.jar exists
RUN ls -la gradle/wrapper/

# Make gradlew executable and build
RUN chmod +x gradlew && ./gradlew build -x test --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/todo-api-0.0.1-SNAPSHOT.jar"]
