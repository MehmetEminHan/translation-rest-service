package com.neuroval.translationApi.services.utility;

import java.util.ArrayList;
import java.util.List;

public class SentenceParser {
    public static List<String> parse(String inputText) {

        // Create a List to store the sentences
        List<String> sentencesList = new ArrayList<>();

        // Split the input string by newline (\n) to get individual sentences
        String[] sentencesArray = inputText.split("\n");

        // Add sentences to the List
        for (String sentence : sentencesArray) {

            // Remove leading/trailing spaces
            sentence = sentence.trim();
            if (!sentence.isEmpty()) {

                // Add non-empty sentences to the list
                sentencesList.add(sentence);
            }
        }

        // Print the sentences stored in the List
        for (String sentence : sentencesList) {
            System.out.println(sentence);
        }

        return sentencesList;
    }
}
