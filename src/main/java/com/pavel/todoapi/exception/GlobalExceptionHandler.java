package com.pavel.todoapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Global exception handler for Todo API
 * Provides centralized exception handling across all controllers
 * Returns structured error responses with appropriate HTTP status codes
 * 
 * @author Pavel
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles TodoNotFoundException
     * Returns 404 NOT FOUND with structured error response
     */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFound(TodoNotFoundException ex, WebRequest request) {
        logger.warn("Todo not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "TODO_NOT_FOUND",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles validation errors from @Valid annotations
     * Returns 400 BAD REQUEST with field-specific error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());
        
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Input validation failed",
            details
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles constraint violations (e.g., database constraints)
     * Returns 400 BAD REQUEST with constraint violation details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        
        List<String> details = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            details.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        ErrorResponse error = new ErrorResponse(
            "CONSTRAINT_VIOLATION",
            "Data constraint violation",
            details
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles database integrity violations
     * Returns 409 CONFLICT for data integrity issues
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.warn("Data integrity violation: {}", ex.getMessage());
        
        String message = "Data integrity constraint violated";
        if (ex.getMessage().contains("unique")) {
            message = "Duplicate entry - record already exists";
        } else if (ex.getMessage().contains("foreign key")) {
            message = "Referenced record does not exist";
        }
        
        ErrorResponse error = new ErrorResponse(
            "DATA_INTEGRITY_VIOLATION",
            message,
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles invalid JSON format in request body
     * Returns 400 BAD REQUEST for malformed JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("Invalid JSON format: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_JSON",
            "Invalid JSON format in request body",
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles missing required request parameters
     * Returns 400 BAD REQUEST for missing parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, WebRequest request) {
        logger.warn("Missing request parameter: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "MISSING_PARAMETER",
            "Required parameter '" + ex.getParameterName() + "' is missing",
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles wrong parameter types (e.g., string instead of number)
     * Returns 400 BAD REQUEST for type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Type mismatch error: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "TYPE_MISMATCH",
            "Invalid parameter type for '" + ex.getName() + "'. Expected: " + ex.getRequiredType().getSimpleName(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles unsupported HTTP methods
     * Returns 405 METHOD NOT ALLOWED
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        logger.warn("Method not supported: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "METHOD_NOT_ALLOWED",
            "HTTP method '" + ex.getMethod() + "' not supported for this endpoint",
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    /**
     * Handles external API exceptions (e.g., BookService failures)
     * Returns 503 SERVICE UNAVAILABLE for external service issues
     */
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex, WebRequest request) {
        logger.warn("External API error: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "EXTERNAL_SERVICE_ERROR",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    /**
     * Handles all other unexpected exceptions
     * Returns 500 INTERNAL SERVER ERROR for general failures
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please contact support if the problem persists.",
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}