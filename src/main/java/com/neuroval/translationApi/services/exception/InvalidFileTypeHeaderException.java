package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class InvalidFileTypeHeaderException extends RuntimeException {
    public InvalidFileTypeHeaderException(String fileType) {
        super(ErrorMessage.INVALID_FILETYPE_0.getMessage() + fileType);
    }
}
