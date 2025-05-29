package com.neuroval.translationApi.services.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuroval.translationApi.model.json.Json;
import com.neuroval.translationApi.model.translation.Translation;
import com.neuroval.translationApi.repository.TranslationRepository;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
@Service

public class JsonOperations {

    @Autowired
    TranslationRepository translationRepository;

    private Json json;
    private ObjectMapper objectMapper;
    private String fileFormat;
    private Translation translation;

    private static final Logger logger = LogManager.getLogger(JsonOperations.class);

    // Loads a JSON file uploaded as MultipartFile and returns its contents as a Map
    public Map<String, Object> loadJsonAsMap(MultipartFile file) throws IOException {
        // Create a new ObjectMapper instance (used to parse JSON)
        objectMapper = new ObjectMapper();

        // Create a new json object
        json = new Json();

        // Validate that the uploaded file is not null or empty
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is null or empty");
        }

        // Parse the JSON file and store it inside the 'json' object's translation map
        json.setTranslation(objectMapper.readValue(file.getInputStream(), Map.class));

        // Process and extract target language values from the parsed JSON
        setTargetLanguage();

        // Return the JSON file contents as a Map
        return objectMapper.readValue(file.getInputStream(), Map.class);
    }

    // Extracts all values from the translation map and stores them in a list called targetLanguage
    public List<String> setTargetLanguage() {
        List<String> targetLanguageList = new ArrayList<>();

        // Iterate through all entries in the translation map and collect values
        json.getTranslation().forEach((key, value) -> {
            targetLanguageList.add(value.toString());
        });

        // Store the collected values in the json object's targetLanguage property
        json.setTargetLanguage(targetLanguageList);

        // Return the list of target language strings
        return json.getTargetLanguage();
    }

    public String getFileFormat(MultipartFile file) {
        fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); // Get file format and set to fileFormat;
        logger.info("The uploaded file format is: {}", fileFormat);
        return fileFormat;
    }

    public Translation mapToFileEntity(MultipartFile file) {
        // Create sourceText and targetText lists in method to clear them for every method run
        List<String> sourceText = new ArrayList<>();
        List<String> targetText =  json.getTargetLanguage();

        for(Map.Entry<String, Object> entry: json.getTranslation().entrySet()){
            sourceText.add(entry.getKey());
        }


        // Create a new instance of translation to clear translation object for every method run
        translation = new Translation();

        // Find file type and save to translation file type
        translation.setFileType(translationRepository.findFileTypeRecnumByTypeName(getFileFormat(file).toUpperCase()));

        // Set sourceText and targetText lists into translation object sourceLanguageText and targetLanguageText fields
        translation.setSourceLanguageText(sourceText);
        translation.setTargetLanguageText(targetText);

        return translation;
    }

    public void saveJsonToDatabase(){
        translationRepository.save(translation);
        logger.info("{}Translation (JSON) saved to database!", translation.toString());
    }
}
