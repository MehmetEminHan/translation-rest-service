package com.neuroval.translationApi.model.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INVALID_FILE_TYPE_1("Please upload .png file, Invalid file type: "),
    INVALID_FILE_TYPE_2(", please upload .xliff file!"),
    INVALID_FILE_TYPE_3(", please upload .png file!"),
    CURROPTED_PNG_FILE("Uploaded .png file is corrupted, please upload .png file!"),
    MISSING_XLIFF("Please send first a xliff file to: neuroval/translatition/validation/xliff/upload/translation");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
