# Use a Java 21 image as the base image
FROM openjdk:21-jdk-slim

# Install Maven and Tesseract (if needed)
RUN apt-get update && \
    apt-get install -y maven tesseract-ocr && \
    apt-get clean

# Set the working directory in the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Run Maven to build the project inside the container
RUN ./mvnw clean install

# Check that the build was successful by copying the .jar file
RUN ls -l target/

# Copy the generated .jar file to the container
COPY target/translation-rest-service-0.0.1-SNAPSHOT.jar translation-rest-service.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
