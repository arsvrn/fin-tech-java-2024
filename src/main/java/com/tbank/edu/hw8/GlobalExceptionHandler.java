package com.tbank.edu.hw8;

import com.tbank.edu.hw8.exception.CurrencyNotFoundException;
import com.tbank.edu.hw8.exception.ServiceUnavailableException;
import com.tbank.edu.hw8.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFound(CurrencyNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "3600");

        ErrorResponse error = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage());
        return new ResponseEntity<>(error, headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse error = new ErrorResponse(ex.getStatusCode().value(), ex.getReason());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}
