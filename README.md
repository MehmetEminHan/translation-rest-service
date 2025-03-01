# Java Language Translation Rest Service 

## Project Overview
This project is a **RESTful** web service developed using **Spring Boot** and **Java**. It offers API endpoints to serialize a provided translation file along with the associated application screenshot, compare them, and identify untranslated segments for translation testing.

## Features
- Java 21
- Spring Boot 3.4.3 
- Spring Boot framework
- Maven 4.0.0
- Lombok 1.18.30
- Jakarta xml bind 4.0.0
- Tesseract OCR 5.11.0
- Log4j 1.2.17

## Prerequisites
--------------->                 Will be added in the future!

## Project Structure
```
translationApi/
│── src/
│   ├── main/
│   │   ├── java/com/neuroval/translationApi
│   │   │   ├── rest/          # REST Controllers
│   │   │   ├── services/      # Business logic
│   │   │   ├── repository/    # Data access layer
│   │   │   ├── model/         # Entity and DTO classes
│   │   │   ├── config/        # Configuration classes
│   │   │   ├── TranslationApiApplication.java  # Main Spring Boot entry point
│   │   ├── resources/
│   │       ├── application.yml  # Configuration file
│   ├── test/java/com/example/   # Unit and integration tests
│── pom.xml       # Maven configuration
│── README.md     # Project documentation
```
## XLIFF - API Endpoints 

| Method | Endpoint                                                    | Description                                              |
|--------|-------------------------------------------------------------|----------------------------------------------------------|
| POST   | /neuroval/translatition/validation/xliff/upload/image       | Detect text from image and serialize them to Java object |
| POST   | /neuroval/translatition/validation/xliff/upload/translation | Serialize translation XLIFF file                         |
| GET    | /neuroval/translatition/validation/xliff/upload/compare     | Compare serialized translation file and uploaded image    |

