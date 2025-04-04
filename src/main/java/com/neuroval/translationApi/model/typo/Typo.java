package com.neuroval.translationApi.model.typo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class Typo {

    private List<String> text;
    private Map<String,List<String>> suggestedCorrection;

}
