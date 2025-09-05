package com.pavel.todoapi.exception;

/**
 * Custom exception for external API communication failures
 * Used when BookService or other external integrations fail
 * Provides specific error context for external service issues
 * 
 * @author Pavel
 * @since 1.0
 */
public class ExternalApiException extends RuntimeException {

    /** The name of the external service that failed */
    private final String serviceName;
    
    /** The HTTP status code from the external service (if available) */
    private final Integer statusCode;

    /**
     * Constructor with service name and message
     * 
     * @param serviceName the name of the external service
     * @param message the error message
     */
    public ExternalApiException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = null;
    }

    /**
     * Constructor with service name, message and status code
     * 
     * @param serviceName the name of the external service
     * @param message the error message
     * @param statusCode the HTTP status code from external service
     */
    public ExternalApiException(String serviceName, String message, Integer statusCode) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }

    /**
     * Constructor with service name, message and cause
     * 
     * @param serviceName the name of the external service
     * @param message the error message
     * @param cause the underlying cause
     */
    public ExternalApiException(String serviceName, String message, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.statusCode = null;
    }

    /**
     * Gets the service name that caused the exception
     * 
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Gets the HTTP status code from the external service
     * 
     * @return the status code, or null if not available
     */
    public Integer getStatusCode() {
        return statusCode;
    }
}