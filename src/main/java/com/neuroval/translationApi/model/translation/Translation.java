package com.neuroval.translationApi.model.translation;

import com.neuroval.translationApi.services.utility.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Table(name = "Translation")
@Data
@Component
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recnum;

    @Column(name = "target_lan_text")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> targetLanguageText;

    @Column(name = "source_lan_text")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> sourceLanguageText;

    @Column(name = "FILE_TYPE")
    private Integer fileType;

}
