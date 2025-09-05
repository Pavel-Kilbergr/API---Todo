package com.pavel.todoapi.exception;

/**
 * Custom exception for Todo not found scenarios
 * Extends RuntimeException to allow unchecked exception handling
 * Used when Todo operations fail due to non-existent Todo ID
 * 
 * @author Pavel
 * @since 1.0
 */
public class TodoNotFoundException extends RuntimeException {

    /** The ID of the Todo that was not found */
    private final Long todoId;

    /**
     * Constructor with Todo ID
     * Creates a meaningful error message including the specific ID
     * 
     * @param id the ID of the Todo that was not found
     */
    public TodoNotFoundException(Long id) {
        super("Todo with ID " + id + " was not found");
        this.todoId = id;
    }

    /**
     * Constructor with custom message
     * Allows for custom error messages while maintaining the same exception type
     * 
     * @param message custom error message
     */
    public TodoNotFoundException(String message) {
        super(message);
        this.todoId = null;
    }

    /**
     * Constructor with ID and custom message
     * Provides both the ID and a custom message for detailed error reporting
     * 
     * @param id the ID of the Todo that was not found
     * @param message custom error message
     */
    public TodoNotFoundException(Long id, String message) {
        super(message);
        this.todoId = id;
    }

    /**
     * Gets the Todo ID that caused the exception
     * 
     * @return the Todo ID, or null if not specified
     */
    public Long getTodoId() {
        return todoId;
    }
}