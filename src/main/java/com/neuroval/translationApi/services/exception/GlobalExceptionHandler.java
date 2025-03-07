package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorDetail> handleInvalidFileTypeException(InvalidFileTypeException ex) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingXliffException.class)
    public ResponseEntity<ErrorDetail> handleMissingXliffException(MissingXliffException ex) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return  new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
