# Use Java 21 image
FROM openjdk:21-jdk-slim

# Install Maven (to build the app in the container)
RUN apt-get update && \
    apt-get install -y maven tesseract-ocr && \
    apt-get clean

# Set the working directory
WORKDIR /app

# Copy your entire project into the container
COPY . .

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw

RUN chmod +x ./mvnw
# Build the project (this will create the target/ directory inside the container)
RUN ./mvnw clean install

# Copy the JAR file to the container
COPY target/translation-rest-service-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
