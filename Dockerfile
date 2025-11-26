# --- Stage 1: Build (opcional si ya tenés jar) ---
# FROM maven:3.9.0-eclipse-temurin-17 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/SantiMarket-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app/app.jar
COPY .env .env


EXPOSE 5432

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
