package com.neuroval.translationApi.model.json;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class Json {
    private Map<String, Object> translation;
    private List<String> targetLanguage;
}
