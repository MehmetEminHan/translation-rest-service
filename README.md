# Java Language Translation Rest Service

## Project Overview

This project is a **RESTful** web service developed using **Spring Boot** and **Java**. It offers API endpoints to
serialize a provided translation file along with the associated application screenshot, compare them, and identify
untranslated segments for translation testing.

## Features

- Java 21
- Spring Boot 3.4.3
- Maven 4.0.0
- Lombok 1.18.30
- Jakarta xml bind 4.0.0
- Tesseract OCR 5.11.0
- Log4j 1.2.17
- LanguageTool 5.9 / LanguageToolAll 5.9

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

| Method | Endpoint                                     | Headers                               | Form-Data         | Description                                                                                                                                              |
|--------|----------------------------------------------|---------------------------------------|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | /translation/validation/xliff/upload/image   | LanguageCode: tesseract language code | image: .png file  | Detects and extracts text from a PNG image using Tesseract OCR. The LanguageCode must match the image's language and be a valid Tesseract language code. |
| POST   | /translation/validation/xliff/upload         | N/A                                   | file: .xliff file | Extracts all target language words/strings from the uploaded XLIFF translation file.                                                                     |
| GET    | /translation/validation/comparison/compare   | N/A                                   | N/A               | Compare serialized translation file and uploaded image                                                                                                   |
| POST   | /translation/validation/xliff/upload/isAwake | N/A                                   | N/A               | Returns status of the endpoint                                                                                                                           |
| GET    | /translation/validation/image/typo           | N/A                                   | N/A               | Detects typo in the uploaded image                                                                                                                       |

## Notes for Dev

- Each time the user makes a new request, the entity corresponding to the desired operation in the services is
  re-initialized in the "mapTo____Entity" method.
- And within each service, these entities are kept in the field without being initialized.
- In short, when we want to access entities between classes, instead of directly calling the Entities themselves, we
  call the entities that we define as fields within the Services.
- The reason for doing this is that @Autowired is a singleton pattern. Since @Autowired keeps the same entity in RAM
  each time, the Hibernate can't save the entities as a different row.