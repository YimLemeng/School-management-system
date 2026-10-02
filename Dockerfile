# Stage 1: Build with Maven & Java 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy POM and sources
COPY pom.xml .
COPY src ./src

# Build production JAR skipping test execution
RUN mvn clean package -DskipTests

# Stage 2: Production JRE runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR artifact from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
