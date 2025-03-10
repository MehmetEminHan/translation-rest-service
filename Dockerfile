FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Install Tesseract 5.5.0 from GitHub
FROM openjdk:21-jdk-slim

# Install dependencies for building Tesseract
RUN apt-get update && apt-get install -y \
    wget \
    git \
    build-essential \
    libleptonica-dev \
    pkg-config \
    libcairo2-dev \
    pango1.0-dev \
    icu-devtools \
    libcurl4-openssl-dev \
    libtool \
    automake \
    autoconf \
    zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

# Clone the Tesseract repository and checkout the 5.5.0 release
RUN git clone https://github.com/tesseract-ocr/tesseract.git /tesseract && \
    cd /tesseract && \
    git checkout 5.5.0

# Build and install Tesseract
RUN cd /tesseract && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install

# Copy the built jar file
COPY --from=build /target/translation-rest-service-0.0.1-SNAPSHOT.jar translation-rest-service.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "translation-rest-service.jar"]
