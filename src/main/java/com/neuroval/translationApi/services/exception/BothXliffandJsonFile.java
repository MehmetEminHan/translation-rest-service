package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorMessage;

public class BothXliffandJsonFile extends RuntimeException{
    public BothXliffandJsonFile(){
        super(ErrorMessage.BOTH_XLIFF_JSON_FILE.getMessage());
    }
}
