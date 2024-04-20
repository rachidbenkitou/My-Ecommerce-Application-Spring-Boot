package com.benkitoucoders.ecommerce.handlers;

import com.benkitoucoders.ecommerce.exceptions.ApiBasedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArguments(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler(ApiBasedException.class)
    public ResponseEntity<ErrorDetails> handleApiExceptions(ApiBasedException cx, WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(cx.getMessage())
                .url(request.getDescription(false))
                .timeStamp(new Date())
                .build();
        return new ResponseEntity<>(errorDetails, cx.getStatusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleTokenValidationException(RuntimeException ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
