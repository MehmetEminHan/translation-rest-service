package com.neuroval.translationApi.services.utility;

import java.util.*;

public class SentenceParser {
    public static List<String> parse(String inputText){

        // Create a List to store the sentences
        List<String> sentencesList = new ArrayList<>();

        // Split the input string by newline (\n) to get individual sentences
        String[] sentencesArray = inputText.split("\n");

        // Add sentences to the List
        for (String sentence : sentencesArray) {
            sentence = sentence.trim();  // Remove leading/trailing spaces
            if (!sentence.isEmpty()) {
                sentencesList.add(sentence);  // Add non-empty sentences to the list
            }
        }

        // Print the sentences stored in the List
        for (String sentence : sentencesList) {
            System.out.println(sentence);
        }

        return sentencesList;
    }
}
