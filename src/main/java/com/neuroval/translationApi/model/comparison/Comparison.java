package com.neuroval.translationApi.model.comparison;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Comparison {
    private List<String> UnmatchedWordsFromXliff;
    private List<String> UnmatchedWordsFromImage;
    private List<String> MatchedWords;
    private String imageWords;
    private List<?> xliffWords;

    @Override
    public String toString() {
        return "Comparison{" +
                "Unmatched Words From Xliff: " + UnmatchedWordsFromXliff +
                ", Unmatched Words From Image: " + UnmatchedWordsFromImage +
                ", Matched Words: " + MatchedWords +
                '}';
    }
}
