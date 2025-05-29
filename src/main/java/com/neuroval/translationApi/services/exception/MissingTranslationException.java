package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class MissingTranslationException extends RuntimeException {
    public MissingTranslationException() {
        super(ErrorMessage.MISSING_TRANSLATION.getMessage());
    }
}
