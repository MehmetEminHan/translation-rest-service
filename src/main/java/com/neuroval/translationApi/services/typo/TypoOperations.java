package com.neuroval.translationApi.services.typo;

import com.neuroval.translationApi.model.typo.Typo;
import com.neuroval.translationApi.rest.image.ImageController;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.utility.SentenceParser;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.language.*;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Service
public class TypoOperations {

    @Autowired
    private Typo typo;
    @Autowired
    private ImageOperations imageOperations;

    private JLanguageTool langTool;
    private Map<String, List<String>> suggestedCorrection;
    private static final Logger logger = LogManager.getLogger(ImageController.class);
    private List<String> imageTextList;


    /**
     * @param imageText
     * @return
     * @throws IOException
     */
    public Map<String, List<String>> typoFinderEnglish(String imageText) throws IOException {

        // Initialize suggested correction hashmap
        suggestedCorrection = new HashMap<>();

        // Map the parsed text by sentences to imageTextList object
        this.imageTextList = SentenceParser.parse(imageText);

        // Return the language code from user (it will be identified in request header in image-upload request by user) and initialize jLanguageTool based on provided language
        setLanguage(imageOperations.getImage().getLanguageCode());

        // imageText has escape sequences as text in it, parse the image text sentences by sentences and foreach them
        for (String text : SentenceParser.parse(imageText)) {
            try {

                // Check the text
                List<RuleMatch> matches = langTool.check(text);

                // Print out the matches (errors)
                for (RuleMatch match : matches) {

                    // Get the start and end positions of the misspelled word
                    String misspelledWord = text.substring(match.getFromPos(), match.getToPos());

                    logger.warn("Potential error at line {}, column {}", match.getLine(), match.getColumn());
                    logger.warn("Message: {}", match.getMessage());
                    logger.warn("Suggested correction: {}", match.getSuggestedReplacements());

                    suggestedCorrection.put(misspelledWord, match.getSuggestedReplacements());
                    System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return suggestedCorrection;
    }

    /**
     * @param word
     */
    public void setLanguage(String word) {
        switch (word) {
            case "eng":
                langTool = new JLanguageTool(new AmericanEnglish());
                break;
            case "jpn":
                langTool = new JLanguageTool(new Japanese());
                break;
            case "rus":
                langTool = new JLanguageTool(new Russian());
                break;
            case "fra":
                langTool = new JLanguageTool(new French());
                break;
            case "deu":
                langTool = new JLanguageTool(new German());
                break;
            default:
                langTool = new JLanguageTool(new AmericanEnglish()); // Fallback
        }
    }

    /**
     * map the corresponding typo entities to the corresponding TYPO entity
     */
    public void mapToTypoEntity() {
        typo = new Typo(); // Initialize the typo entity for each request

        typo.setText(imageTextList);
        typo.setSuggestedCorrection(suggestedCorrection);

        logger.info("Founded typo successfully mapped to TYPO entity", typo.toString());
    }
}
