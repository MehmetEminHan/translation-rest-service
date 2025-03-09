package com.neuroval.translationApi.services.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    // Method to get the logger instance
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}

