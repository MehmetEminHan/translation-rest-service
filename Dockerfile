FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

# Install necessary dependencies for Tesseract
RUN apt-get update && apt-get install -y \
    libleptonica-dev \
    tesseract-ocr=5.5.0-1 \

COPY --from=build /target/translation-rest-service-0.0.1-SNAPSHOT.jar translation-rest-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","translation-rest-service.jar"]




