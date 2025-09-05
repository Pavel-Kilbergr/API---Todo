package com.pavel.todoapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a Todo/Task item in the application
 * Includes automatic timestamp management and validation constraints
 * 
 * @author Pavel
 * @since 1.0
 */
@Entity
@Table(name = "todos")
public class Todo {
    
    /** Primary key - auto-generated unique identifier */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Todo title - required field with max 100 characters */
    @NotBlank(message = "Title is required and cannot be blank")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;
    
    /** Todo description - optional field with max 500 characters */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;
    
    /** Completion status - defaults to false (pending) */
    @NotNull(message = "Completed status is required")
    @Column(nullable = false)
    private Boolean completed = false;
    
    /** Creation timestamp - automatically set on entity creation */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    /** Last update timestamp - automatically set on entity modification */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Default constructor
     * Automatically sets creation timestamp to current time
     */
    public Todo() {
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with title and description
     * 
     * @param title the todo title
     * @param description the todo description
     */
    public Todo(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }
    
    /**
     * JPA lifecycle callback - automatically updates timestamp before entity update
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    /**
     * Gets the unique identifier of this todo
     * @return the todo ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the unique identifier of this todo
     * @param id the todo ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the title of this todo
     * @return the todo title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of this todo
     * @param title the todo title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the description of this todo
     * @return the todo description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of this todo
     * @param description the todo description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the completion status of this todo
     * @return true if completed, false if pending
     */
    public Boolean getCompleted() {
        return completed;
    }
    
    /**
     * Sets the completion status of this todo
     * @param completed true if completed, false if pending
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    /**
     * Gets the creation timestamp
     * @return when this todo was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the creation timestamp
     * @param createdAt when this todo was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the last update timestamp
     * @return when this todo was last updated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
