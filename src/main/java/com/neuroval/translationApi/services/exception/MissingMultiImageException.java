package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class MissingMultiImageException extends RuntimeException {
    public MissingMultiImageException() {
        super(ErrorMessage.MISSING_MULTI_IMAGE.getMessage());
    }
}
