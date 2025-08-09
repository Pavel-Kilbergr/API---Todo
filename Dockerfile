# Dockerfile pro nasazení na web
FROM openjdk:17-jdk-slim

# Pracovní složka
WORKDIR /app

# Zkopírovat Gradle wrapper a build soubory
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle/

# Zkopírovat zdrojové kódy
COPY src src/

# Sestavit aplikaci
RUN ./gradlew build -x test

# Spustit aplikaci
EXPOSE 8080
CMD ["java", "-jar", "build/libs/todo-api-0.0.1-SNAPSHOT.jar"]
