# Build stage
 FROM maven:3.9.8-eclipse-temurin-21 AS build
 COPY . .
 RUN mvn clean package -DskipTests

 # Install Tesseract 5.5.0 from GitHub
 FROM openjdk:21-jdk-slim

 # Install Tesseract OCR
 RUN apt-get update && \
     apt-get install -y tesseract-ocr && \
     apt-get install -y tesseract-ocr tesseract-ocr-all && \
     rm -rf /var/lib/apt/lists/*

 # Copy the built jar file from the build stage
 COPY --from=build /target/translation-rest-service-0.0.1-SNAPSHOT.jar translation-rest-service.jar

 # Expose port 8080
 EXPOSE 8080

 # Run the Spring Boot application
 ENTRYPOINT ["java", "-jar", "translation-rest-service.jar"]