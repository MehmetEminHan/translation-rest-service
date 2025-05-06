package com.neuroval.translationApi.services.exception;

import com.neuroval.translationApi.model.exception.ErrorDetail;
import com.neuroval.translationApi.services.log.Log;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Log.getLogger(GlobalExceptionHandler.class);  // Logger initialized for this class only once
    private ErrorDetail errorDetail;

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorDetail> handleInvalidFileTypeException(InvalidFileTypeException ex) {
        errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()); // set error detail
        logger.error(errorDetail.toString()); // log error detail
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingXliffException.class)
    public ResponseEntity<ErrorDetail> handleMissingXliffException(MissingXliffException ex) {
        errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()); // set error detail
        logger.error(errorDetail.toString()); // log error detail
        return  new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CorruptedFileException.class)
    public ResponseEntity<ErrorDetail> handleCorruptedFileException(CorruptedFileException ex) {
        errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()); // set error detail
        logger.error(errorDetail.toString()); // log error detail
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingImageException.class)
    public ResponseEntity<ErrorDetail> handleMissingImageException(MissingImageException ex) {
        errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()); // set error detail
        logger.error(errorDetail.toString()); // log error detail
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
