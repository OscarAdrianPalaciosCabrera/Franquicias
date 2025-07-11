
FROM maven:3.9.0-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src/ /app/src/

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app


COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]