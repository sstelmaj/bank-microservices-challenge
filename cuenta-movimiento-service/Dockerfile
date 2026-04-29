FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml ./
COPY cuenta-movimiento-service/pom.xml cuenta-movimiento-service/pom.xml
COPY cuenta-movimiento-service/src cuenta-movimiento-service/src

RUN mvn -f cuenta-movimiento-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /workspace/cuenta-movimiento-service/target/cuenta-movimiento-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
