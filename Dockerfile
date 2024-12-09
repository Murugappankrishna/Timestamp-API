FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . /app
RUN mvn clean package
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/Timestamp-Microservice-0.0.1-SNAPSHOT.jar /app/my-app.jar
CMD ["java", "-jar", "my-app.jar"]