package com.pavel.todoapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response structure for Todo API
 * Provides consistent error information format across all endpoints
 * Includes error codes, messages, timestamps and optional details
 * 
 * @author Pavel
 * @since 1.0
 */
public class ErrorResponse {
    
    /** Unique error code for programmatic error identification */
    private String errorCode;
    
    /** Human-readable error message for developers/users */
    private String message;
    
    /** Timestamp when the error occurred */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    /** Optional detailed error information (validation errors, stack traces, etc.) */
    private List<String> details;
    
    /** HTTP path where the error occurred */
    private String path;

    /**
     * Default constructor
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for basic error response
     * 
     * @param errorCode unique error identifier
     * @param message human-readable error message
     */
    public ErrorResponse(String errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Constructor for error response with details
     * 
     * @param errorCode unique error identifier
     * @param message human-readable error message
     * @param details additional error details
     */
    public ErrorResponse(String errorCode, String message, List<String> details) {
        this(errorCode, message);
        this.details = details;
    }

    /**
     * Constructor for error response with path information
     * 
     * @param errorCode unique error identifier
     * @param message human-readable error message
     * @param path HTTP path where error occurred
     */
    public ErrorResponse(String errorCode, String message, String path) {
        this(errorCode, message);
        this.path = path;
    }

    // Getters and Setters
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", details=" + details +
                '}';
    }
}
