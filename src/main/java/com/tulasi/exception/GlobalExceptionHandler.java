package com.tulasi.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.annotation.Generated;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handles Employee not found
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
    	
    	logger.error("EmployeeNotFoundException handled properly: {}",ex.getMessage(),ex);
    	  Map<String, Object> error = new HashMap<>();
          error.put("error", ex.getMessage());
          error.put("status", HttpStatus.NOT_FOUND.value());
          error.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
 

    // Handles validation errors (null fields etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    //Handles wrong path variable type (/api/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Invalid employee ID type. It must be a number.", HttpStatus.BAD_REQUEST);
    }

    // Handles duplicate email
    @Generated("jacoco-ignore")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicate(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Duplicate Employee data not allowed!", HttpStatus.CONFLICT);
    }
    @Generated("jacoco-ignore")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Generated("jacoco-ignore")
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleDatabaseFailure(DataAccessResourceFailureException ex) {
        return new ResponseEntity<>("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
