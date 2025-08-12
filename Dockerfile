FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy source files
COPY . .

# Build JAR and run in one step (no intermediate files)
RUN chmod +x gradlew && \
    ./gradlew build -x test --no-daemon && \
    cp build/libs/todo-api-0.0.1-SNAPSHOT.jar app.jar && \
    rm -rf build gradle .gradle src

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
