package com.neuroval.translationApi.model.exception;


import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDetail {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;

    public ErrorDetail(int statusCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
    }
}
