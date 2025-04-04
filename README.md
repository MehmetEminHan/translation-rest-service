# Java Language Translation Rest Service 

## Project Overview
This project is a **RESTful** web service developed using **Spring Boot** and **Java**. It offers API endpoints to serialize a provided translation file along with the associated application screenshot, compare them, and identify untranslated segments for translation testing.

## Features
- Java 21
- Spring Boot 3.4.3
- Maven 4.0.0
- Lombok 1.18.30
- Jakarta xml bind 4.0.0
- Tesseract OCR 5.11.0
- Log4j 1.2.17

## Prerequisites
- Download tesseract in https://github.com/tesseract-ocr/tesseract

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
| GET    | /neuroval/translatition/validation/xliff/upload/compare     | Compare serialized translation file and uploaded image   |
| POST   | /neuroval/translatition/validation/xliff/upload/isAwake     | Returns status of the endpoint                           |

## Notes for Dev
- Each time the user makes a new request, the entity corresponding to the desired operation in the services is re-initialized in the "mapTo____Entity" method.
- And within each service, these entities are kept in the field without being initialized.
- In short, when we want to access entities between classes, instead of directly calling the Entities themselves, we call the entities that we define as fields within the Services.
- The reason for doing this is that @Autowired is a singleton pattern. Since @Autowired keeps the same entity in RAM each time, the Hibernate can't save the entities as a different row.