package com.pavel.todoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for Todo REST API
 * Entry point that bootstraps the entire application with auto-configuration
 * 
 * Features:
 * - REST API endpoints for todo management
 * - H2 in-memory database with JPA
 * - Automatic data initialization
 * - Cross-origin support for web clients
 * - Book API integration capabilities
 * 
 * @author Pavel
 * @since 1.0
 */
@SpringBootApplication
public class TodoApiApplication {

	/**
	 * Application entry point
	 * Starts embedded Tomcat server and initializes Spring context
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

}
