package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

import javax.imageio.IIOException;

public class CorruptedFileException extends IIOException {
    public CorruptedFileException(String exceptionMessage) {
        super(ErrorMessage.CORRUPTED_PNG_FILE.getMessage() + " Exception:" +exceptionMessage);
    }
}
