package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class MissingXliffException extends RuntimeException {
    public MissingXliffException() {
        super(ErrorMessage.MISSING_XLIFF.getMessage());
    }
}
