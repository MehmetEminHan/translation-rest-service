package com.neuroval.translationApi.model.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INVALID_FILE_TYPE_1("Invalid file: "),
    INVALID_FILE_TYPE_2(", please upload .xliff file!"),
    MISSING_XLIFF("Please send first a xliff file to: neuroval/translatition/validation/xliff/upload/translation");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
