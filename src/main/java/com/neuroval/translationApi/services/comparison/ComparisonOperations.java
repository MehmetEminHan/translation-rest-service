package com.neuroval.translationApi.services.comparison;

import com.neuroval.translationApi.model.comparison.Comparison;
import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.image.Image;
import com.neuroval.translationApi.model.xliff.xliff_1_2.Xliff_1_2;
import com.neuroval.translationApi.model.xliff.xliff_2_0.Xliff_2_0;
import com.neuroval.translationApi.repository.ComparisonRepository;
import com.neuroval.translationApi.services.image.ImageOperations;
import com.neuroval.translationApi.services.xliff.XliffOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ComparisonOperations {

    @Autowired
    private Comparison comparison;
    @Autowired
    private Xliff xliff;
    @Autowired
    private Xliff_1_2 xliff_1_2;
    @Autowired
    private Xliff_2_0 xliff_2_0;
    @Autowired
    private Image image;
    @Autowired
    private XliffOperations xliffOperations;
    @Autowired
    private ImageOperations imageOperations;

    @Autowired
    ComparisonRepository comparisonRepository;

    private static final Logger logger = Logger.getLogger(ComparisonOperations.class.getName());
    List<String> xliffTargetTextList;
    List<String> imageTextList;
    List<String> matchedWords;

    /**
     * Compare serialized XLIFF file and serialized uploaded screenshot and return non-matched words
     * @return
     */
    public Object compareXliffAndImage() {
        imageTextList = new ArrayList<>(image.getTextList());
        matchedWords = new ArrayList<>();
        xliffTargetTextList = new ArrayList<>();

        String targetText = "";

        for (int i = 0; i < getTransUnitListSize(); i++) {
            targetText = getTargetText(i);
            String[] words = targetText.split("\\s+"); // Splits the target text by whitespace
            for (String word : words) {
                xliffTargetTextList.add(word); // Add each word individually
            }
        }

        logger.info("----------------------COMPARISON---------------------");
        logger.info("XLIFF:" + xliffTargetTextList);
        logger.info("IMAGE:" + imageTextList);
        logger.info("Comparison Result");

        // Find matched words and set them to matchedWords List
        if (xliffTargetTextList.size() < imageTextList.size()) {
            for (String s : imageTextList) {
                if (xliffTargetTextList.contains(s)) {
                    matchedWords.add(s);
                }
            }
        } else {
            for (String s : xliffTargetTextList) {
                if (imageTextList.contains(s)) {
                    matchedWords.add(s);
                }
            }
        }


        // Remove the same words and collect the unmatched words in the Array
        imageTextList.removeAll(xliffTargetTextList);

        // Remove the same words and collect the unmatched words in the Array
        xliffTargetTextList.removeAll(image.getTextList());

        logger.info(comparison.toString());
        logger.info("----------------------COMPARISON-END---------------------");

        // Check if the trans unit target language list is empty return that
        if (xliffTargetTextList.isEmpty()) {
            xliffTargetTextList.add("All words are matched! congrats!");
        }

        mapToFileEntity();

        return comparison;
    }


    /**
     * Return the target text of the transunit file in the desired index within the specified xliff file.
     * @param i
     * @return
     */
    private String getTargetText(int i) {
        String targetText = "";

        // Create a new list contains transunit target language
        try {
            if (xliff.getFile() != null) {
                targetText = xliff.getFile().getBody().getTransUnitList().get(i).getTarget();

            } else if (xliff_1_2.getFile() != null) {
                targetText = xliff_1_2.getFile().getBody().getTransUnitList().get(i).getTarget();

            } else if (xliff_2_0.getFile() != null) {
                targetText = xliff_2_0.getFile().getBody().getTransUnitList().get(i).getTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetText;
    }

    private int getTransUnitListSize() {
        int transUnitListSize = 0;

        try {
            // Create a new list contains transunit target language
            if (xliff.getFile() != null) {
                transUnitListSize = xliff.getFile().getBody().getTransUnitList().size();
            } else if (xliff_1_2.getFile() != null) {
                transUnitListSize = xliff_1_2.getFile().getBody().getTransUnitList().size();
            } else if (xliff_2_0.getFile() != null) {
                transUnitListSize = xliff_2_0.getFile().getBody().getTransUnitList().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transUnitListSize;
    }

    /**
     * map the corresponding image and xliff entities to the corresponding COMPARISON entity
     */
    public void mapToFileEntity() {
        comparison = new Comparison(); // Create new comparison entity
        comparison.setMatchedWords(matchedWords); // Set map matchedWords list to comparison object matched words
        comparison.setFileLinknum(xliffOperations.getTranslation().getRecnum()); // Set comparison file linknum as trasnlation recnum
        comparison.setImageLinknum(imageOperations.getImage().getRecnum()); // Set comparison image linkum as image recnum
        comparison.setUnmatchedWordsFromXliff(xliffTargetTextList); // Set unmatched words from xliff to comparison UnmatchedWordsFromXliff List
        comparison.setUnmatchedWordsFromImage(imageTextList); // Set unmatched words from image to comparison UnmatchedWordsFromImage List
        comparison.setImageWords(imageOperations.getImage().getText());
    }

    /**
     * Save COMPARISON entity to database
     */
    public void saveComparisonToDatabase() {
        comparisonRepository.save(comparison);
    }
}


