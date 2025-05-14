package com.neuroval.translationApi.model.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INVALID_FILE_TYPE_1("Please upload .png file, Invalid file type: "),
    INVALID_FILE_TYPE_2(", please upload .xliff file!"),
    INVALID_FILE_TYPE_3(", please upload .png file!"),
    CORRUPTED_PNG_FILE("Uploaded .png file is corrupted, please upload .png file!"),
    MISSING_XLIFF("Please send first a xliff file to: /translatition/validation/xliff/upload/translation"),
    MISSING_IMAGE("Please send first a image file to: /translation/validation/image/upload"),
    MISSING_MULTI_IMAGE("Please select at least one image and please send images' files to: /translation/validation/image/multi-upload");
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
