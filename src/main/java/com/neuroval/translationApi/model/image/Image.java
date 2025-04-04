package com.neuroval.translationApi.model.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Entity
@Component
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recnum;

    @Lob
    @Column(name = "EXTRACTED_TEXT", columnDefinition = "LONGBLOB")
    private String text;

    // Ignore this part for saving to database
    @Transient
    private List<String> textList;

    @Lob
    @Column(name = "IMAGE_DATA")
    @JsonIgnore
    private byte[] imageData;

    @Column(name = "IMAGE_TYPE")
    private Integer imageType;

    @Transient
    private String languageCode;
}
