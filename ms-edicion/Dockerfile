# --- Etapa 1: Build (imagen oficial de Maven, no necesita mvnw) ---
FROM --platform=$BUILDPLATFORM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Runtime ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# curl necesario para healthcheck (actuator/health)
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]