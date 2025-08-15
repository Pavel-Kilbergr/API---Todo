package com.pavel.todoapi.config;

import com.pavel.todoapi.entity.Todo;
import com.pavel.todoapi.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Application data initializer component
 * Implements CommandLineRunner to populate database with sample data on startup
 * Useful for development, testing, and demonstration purposes
 * 
 * @author Pavel
 * @since 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /** Dependency injection of TodoRepository for data operations */
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Executes after Spring Boot application startup
     * Creates sample todo data for development and testing
     * 
     * @param args command line arguments (not used)
     * @throws Exception if data initialization fails
     */
    @Override
    public void run(String... args) throws Exception {
        // Clear existing data to ensure clean state on restart
        todoRepository.deleteAll();
        
        // Create sample todos for development/testing
        Todo todo1 = new Todo("Create REST API", "Implement Spring Boot application with CRUD operations");
        
        Todo todo2 = new Todo("Test in Postman", "Test all endpoints using Postman API client");
        
        Todo todo3 = new Todo("Deploy to web", "Deploy application to Render or Railway platform");
        
        Todo todo4 = new Todo("Complete documentation", "Write comprehensive README and API documentation");
        todo4.setCompleted(true); // Mark as completed example
        
        Todo todo5 = new Todo("Add input validation", "Implement validation for request data and error handling");
        
        // Persist sample data to database
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
        todoRepository.save(todo4);
        todoRepository.save(todo5);
        
        System.out.println("✅ Sample data initialized!");
        System.out.println("📊 Created " + todoRepository.count() + " sample todos");
    }
}
