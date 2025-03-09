# Use a lightweight Java image
FROM openjdk:21-jdk-slim

# Install Tesseract OCR
RUN apt-get update && \
    apt-get install -y tesseract-ocr && \
    apt-get clean

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/translation-rest-service-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
