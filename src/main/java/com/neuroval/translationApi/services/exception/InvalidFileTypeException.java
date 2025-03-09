package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String uploadedFileFormat) {
        super(ErrorMessage.INVALID_FILE_TYPE_1.getMessage() + uploadedFileFormat);
    }
}
