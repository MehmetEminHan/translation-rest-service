package com.neuroval.translationApi.model.image;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Image {

    private String text;
    private List<String> textList;
}
