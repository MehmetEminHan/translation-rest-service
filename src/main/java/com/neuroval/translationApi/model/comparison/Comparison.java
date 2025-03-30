package com.neuroval.translationApi.model.comparison;

import com.neuroval.translationApi.services.utility.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Table(name = "comparison")
@Entity
public class Comparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recnum;

    @Column(name = "unmatched_words_file")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> UnmatchedWordsFromXliff;

    @Column(name = "unmatched_words_image")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> UnmatchedWordsFromImage;

    @Column(name = "matched_words")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> MatchedWords;

    @Transient
    private String imageWords;

    @Transient
    private List<?> xliffWords;

    @Column(name = "image_linknum")
    private Integer imageLinknum;

    @Column(name = "file_linknum")
    private Integer fileLinknum;

    @Override
    public String toString() {
        return "Comparison{" +
                "Unmatched Words From Xliff: " + UnmatchedWordsFromXliff +
                ", Unmatched Words From Image: " + UnmatchedWordsFromImage +
                ", Matched Words: " + MatchedWords +
                '}';
    }
}
