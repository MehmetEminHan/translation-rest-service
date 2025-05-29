package com.neuroval.translationApi.services.comparison;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.json.Json;
import com.neuroval.translationApi.model.xliff.Body;
import com.neuroval.translationApi.repository.ComparisonRepository;
import com.neuroval.translationApi.services.exception.BothXliffandJsonFile;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.json.JsonOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
@Service
public class ComparisonOperations {

    @Autowired
    private Comparison comparison;
    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;
    @Autowired
    private JsonOperations jsonOperations;
    @Autowired
    ComparisonRepository comparisonRepository;

    private String fileType;

    private static final Logger logger = Logger.getLogger(ComparisonOperations.class.getName());
    List<String> translationTextList;
    List<String> imageTextList;
    List<String> matchedWords;

    /**
     * Compare serialized XLIFF file and serialized uploaded screenshot and return non-matched words
     *
     * @return
     */
    public Object compareTranslationAndImage() {
        imageTextList = new ArrayList<>(imageOperations.getImage().getTextList());
        matchedWords = new ArrayList<>();
        translationTextList = new ArrayList<>();

        String targetText = "";

        // Entire back end file logic happens in the compareTranslationAndImage method in the ComparisonOperations.java class
        // Entire purpose of this logic if all file formats are uploaded by users, delete them from the memory and ask user the re-upload only one file format at a comparison, otherwise that will cause a backend problems!
        // If xliff file is not NULL and json file is NULL
        if(fileType.equalsIgnoreCase("xliff")){
            for (int i = 0; i < xliffOperations.getBody().getTransUnitList().size(); i++) {

                targetText = xliffOperations.getBody().getTransUnitList().get(i).getTarget();

                // Splits the target text by whitespace
                String[] words = targetText.split("\\s+");
                for (String word : words) {

                    // Add each word individually
                    translationTextList.add(word);
                }
            }


        // If xliff file is NULL and json file is not null
        }else if (fileType.equalsIgnoreCase("json")){
            for (int i = 0; i < jsonOperations.getJson().getTargetLanguage().size(); i++) {
                targetText = jsonOperations.getJson().getTargetLanguage().get(i);

                // Splits the target text by whitespace
                String[] words = targetText.split("\\s+");
                for (String word : words) {

                    // Add each word individually
                    translationTextList.add(word);
                }
            }

        // If xliff file is not NULL and json file is not NULL too
        }else if(xliffOperations.getBody() != null && jsonOperations.getJson() != null){
            // If both xliff file and json file is not null throw an exception and create a new object for both xliff and json to clear the existing fields
            xliffOperations.setBody(null);
            jsonOperations.setJson(null);
            throw new BothXliffandJsonFile();
        }

        logger.info("----------------------COMPARISON---------------------");
        logger.info("TRANSLATION:" + translationTextList);
        logger.info("IMAGE:" + imageTextList);
        logger.info("Comparison Result");

        // Find matched words and set them to matchedWords List
        if (translationTextList.size() < imageTextList.size()) {
            for (String s : imageTextList) {
                if (translationTextList.contains(s)) {
                    matchedWords.add(s);
                }
            }
        } else {
            for (String s : translationTextList) {
                if (imageTextList.contains(s)) {
                    matchedWords.add(s);
                }
            }
        }


        // Remove the same words and collect the unmatched words in the Array
        imageTextList.removeAll(translationTextList);

        // Remove the same words and collect the unmatched words in the Array
        translationTextList.removeAll(imageOperations.getImage().getTextList());

        logger.info(comparison.toString());
        logger.info("----------------------COMPARISON-END---------------------");

        // Check if the trans unit target language list is empty return that
        if (translationTextList.isEmpty()) {
            translationTextList.add("All words are matched! congrats!");
        }

        mapToFileEntity();

        return comparison;
    }



    /**
     * map the corresponding image and xliff entities to the corresponding COMPARISON entity
     */
    public void mapToFileEntity() {
        comparison = new Comparison(); // Create new comparison entity
        if (fileType.equalsIgnoreCase("xliff")) {
            comparison.setMatchedWords(matchedWords); // Set map matchedWords list to comparison object matched words
            comparison.setFileLinknum(xliffOperations.getTranslation().getRecnum()); // Set comparison file linknum as trasnlation recnum
            comparison.setImageLinknum(imageOperations.getImage().getRecnum()); // Set comparison image linkum as image recnum
            comparison.setUnmatchedWordsFromFile(translationTextList); // Set unmatched words from xliff to comparison UnmatchedWordsFromXliff List
            comparison.setUnmatchedWordsFromImage(imageTextList); // Set unmatched words from image to comparison UnmatchedWordsFromImage List
            comparison.setImageWords(imageOperations.getImage().getText());
            comparison.setFileWords(xliffOperations.getTranslation().getTargetLanguageText());
        }else if (fileType.equalsIgnoreCase("json")) {
            comparison.setMatchedWords(matchedWords); // Set map matchedWords list to comparison object matched words
            comparison.setFileLinknum(jsonOperations.getTranslation().getRecnum());// Set comparison file linknum as trasnlation recnum
            comparison.setImageLinknum(imageOperations.getImage().getRecnum()); // Set comparison image linkum as image recnum
            comparison.setUnmatchedWordsFromFile(translationTextList); // Set unmatched words from xliff to comparison UnmatchedWordsFromXliff List
            comparison.setUnmatchedWordsFromImage(imageTextList); // Set unmatched words from image to comparison UnmatchedWordsFromImage List
            comparison.setImageWords(imageOperations.getImage().getText());
            comparison.setFileWords(jsonOperations.getTranslation().getTargetLanguageText());
        }
    }

    /**
     * Save COMPARISON entity to database
     */
    public void saveComparisonToDatabase() {
        comparisonRepository.save(comparison);
    }

}


