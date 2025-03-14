package com.neuroval.translationApi.rest.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object statusCodeObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int statusCode = statusCodeObj != null ? Integer.parseInt(statusCodeObj.toString()) : 500;

        // 🟢 Only handle 404 errors
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("timestamp", LocalDateTime.now());
            errorBody.put("status", 404);
            errorBody.put("error", "Not Found");
            errorBody.put("message", "The requested endpoint does not exist");
            errorBody.put("path", path);
            return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
        }

        // 🟡 Let other errors fall through to Spring Boot default
        return null; // Let Spring handle 500s, 400s, etc. as normal
    }
}
