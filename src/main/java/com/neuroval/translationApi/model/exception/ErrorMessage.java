package com.neuroval.translationApi.model.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INVALID_FILETYPE_0("Invalid file type! Entered file type in the header is: "),
    INVALID_FILE_TYPE_1("Please upload correct translation file, Invalid file type: "),
    INVALID_FILE_TYPE_2(", please upload .xliff file!"),
    INVALID_FILE_TYPE_3(", please upload .png file!"),
    CORRUPTED_PNG_FILE("Uploaded .png file is corrupted, please upload .png file!"),
    MISSING_TRANSLATION("Please send first a TRANSLATION file!"),
    MISSING_IMAGE("Please send first a image file to: /translation/validation/image/upload"),
    MISSING_MULTI_IMAGE("Please select at least one image and please send images' files to: /translation/validation/image/multi-upload"),
    BOTH_XLIFF_JSON_FILE("Both XLIFF JSON files are not supported! Uploaded JSON and XLIFF files has been deleted from memory!");
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
