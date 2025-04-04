package com.neuroval.translationApi.services.typo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.neuroval.translationApi.model.typo.Typo;
import com.neuroval.translationApi.rest.image.ImageController;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.utility.SentenceParser;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.language.*;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Data
@Service
public class TypoOperations {

    @Autowired
    private Typo typo;
    @Autowired
    private ImageOperations imageOperations;

    private JLanguageTool langTool;
    private Map<String,List<String>> suggestedCorrection;
    private static final Logger logger = LogManager.getLogger(ImageController.class);
    private List<String> imageTextList;


    /**
     *
     * @param imageText
     * @return
     * @throws IOException
     */
    public Map<String,List<String>> typoFinderEnglish(String imageText) throws IOException {

        suggestedCorrection = new HashMap<>(); // Initialize suggested correction hashmap
        this.imageTextList = SentenceParser.parse(imageText); // Map the parsed text by sentences to imageTextList object

        setLanguage(imageOperations.getImage().getLanguageCode()); // Return the language code from user (it will be identified in request header in image-upload request by user) and initialize jLanguageTool based on provided language

        // imageText has escape sequences as text in it, parse the image text sentences by sentences and foreach them
        for (String text : SentenceParser.parse(imageText)) {
            try {
                List<RuleMatch> matches = langTool.check(text); // Check the text

                // Print out the matches (errors)
                for (RuleMatch match : matches) {
                    String misspelledWord = text.substring(match.getFromPos(), match.getToPos());  // Get the start and end positions of the misspelled word

                    logger.warn("Potential error at line {}, column {}", match.getLine(), match.getColumn());
                    logger.warn("Message: {}", match.getMessage());
                    logger.warn("Suggested correction: {}", match.getSuggestedReplacements());

                    suggestedCorrection.put(misspelledWord,match.getSuggestedReplacements());
                    System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return suggestedCorrection;
    }


    /**
     *
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

    public void mapToTypoEntity(){
        typo = new Typo(); // Initialize the typo entity for each request

        typo.setText(imageTextList);
        typo.setSuggestedCorrection(suggestedCorrection);

        logger.info("Founded typo successfully mapped to TYPO entity", typo.toString());
    }

}
