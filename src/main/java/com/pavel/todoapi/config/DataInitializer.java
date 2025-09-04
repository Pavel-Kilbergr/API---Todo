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
     * Data initializer is now disabled for production use
     * 
     * @param args command line arguments (not used)
     * @throws Exception if data initialization fails
     */
    @Override
    public void run(String... args) throws Exception {
        // Data initialization disabled for production
        // Database will start empty, ready for real user data
        System.out.println("✅ DataInitializer: No sample data created (production mode)");
        System.out.println("📊 Current todos in database: " + todoRepository.count());
    }
}
