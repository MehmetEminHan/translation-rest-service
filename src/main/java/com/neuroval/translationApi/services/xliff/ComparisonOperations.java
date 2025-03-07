package com.neuroval.translationApi.services.xliff;

import com.neuroval.translationApi.model.xliff.Xliff;
import com.neuroval.translationApi.model.image.Image;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComparisonOperations {

    // Compare serialized XLIFF file and serialized uploaded screenshot and return non-matched words
    public List<String> compareXliffAndImage(Image image, Xliff xliff){
        List<String> transUnitTargetLanguageList = new ArrayList<>();

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

        // Check if the trans unit target language list is empty return that
        if(transUnitTargetLanguageList.isEmpty()){
            transUnitTargetLanguageList.add("All words are matched! congrats!");
        }

        return transUnitTargetLanguageList;
    }
}


