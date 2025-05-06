package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class MissingImageException extends RuntimeException {
    public MissingImageException() {
        super(ErrorMessage.MISSING_IMAGE.getMessage());
    }
}
