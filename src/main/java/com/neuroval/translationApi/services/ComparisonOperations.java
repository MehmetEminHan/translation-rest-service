package com.neuroval.translationApi.services;

import com.neuroval.translationApi.model.XLIFF.Xliff;
import com.neuroval.translationApi.model.image.Image;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComparisonOperations {

    public List<String> compareXliffAndImage(Image image, Xliff xliff){
        List<String> transUnitTargetLanguageList = new ArrayList<>();
        List<String> differenceBetweenImageAndXliff = new ArrayList<>();

        // Create a new list contains transunit target language
        for (int i = 0; i < xliff.getFile().getBody().getTransUnitList().size() ; i++) {
            String targetText = xliff.getFile().getBody().getTransUnitList().get(i).getTarget();
            String[] words = targetText.split("\\s+"); // Splits the target text by whitespace
            for (String word : words) {
                transUnitTargetLanguageList.add(word); // Add each word individually
            }
        }

        // Remove the same words and collect the unmatched words in the Array
        transUnitTargetLanguageList.removeAll(image.getTextList());


        return transUnitTargetLanguageList;
    }
}
