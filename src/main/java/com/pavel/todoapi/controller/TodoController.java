package com.pavel.todoapi.controller;

import com.pavel.todoapi.entity.Todo;
import com.pavel.todoapi.exception.TodoNotFoundException;
import com.pavel.todoapi.repository.TodoRepository;
import com.pavel.todoapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller providing HTTP endpoints for Todo API operations
 * Implements full CRUD functionality with additional features like search and statistics
 * Supports JSON request/response format with proper HTTP status codes
 * 
 * @author Pavel
 * @since 1.0
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // Allow all origins for development/testing
@Validated
public class TodoController {

    /** Dependency injection of TodoRepository for data access operations */
    @Autowired
    private TodoRepository todoRepository;
    
    /** Dependency injection of BookService for external Book API integration */
    @Autowired
    private BookService bookService;

    /**
     * Provides API information and statistics
     * Returns comprehensive metadata about the API including usage statistics
     * 
     * @return ResponseEntity containing API info and current statistics
     * @apiNote GET /api/todos/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Todo REST API");
        info.put("version", "1.0.0");
        info.put("description", "Simple Todo API built with Spring Boot & Gradle");
        info.put("author", "Pavel");
        info.put("totalTodos", todoRepository.count());
        info.put("completedTodos", todoRepository.countCompletedTodos());
        info.put("pendingTodos", todoRepository.countPendingTodos());
        info.put("endpoints", List.of(
            "GET /api/todos - Get all todos",
            "GET /api/todos/{id} - Get todo by ID",
            "POST /api/todos - Create new todo",
            "PUT /api/todos/{id} - Update todo",
            "DELETE /api/todos/{id} - Delete todo",
            "GET /api/todos/completed - Get completed todos",
            "GET /api/todos/pending - Get pending todos",
            "GET /api/todos/search?title=... - Search todos by title"
        ));
        return ResponseEntity.ok(info);
    }

    /**
     * Get all todos
     * GET /api/todos
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return ResponseEntity.ok(todos);
    }

    /**
     * Get todo by ID with proper exception handling
     * Throws TodoNotFoundException if todo doesn't exist
     * 
     * @param id the todo ID to retrieve
     * @return ResponseEntity with the found todo
     * @throws TodoNotFoundException if todo with given ID doesn't exist
     * @apiNote GET /api/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        return ResponseEntity.ok(todo);
    }

    /**
     * Creates a new todo with validation and optional Book Checker functionality
     * If title is "BOOK_CHECKER" and description is integer, fetches book info from external API
     * 
     * @param todo the todo object to create (must be valid)
     * @return ResponseEntity with created todo
     * @apiNote POST /api/todos
     */
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        // Check if this is a Book Checker request
        if ("BOOK_CHECKER".equals(todo.getTitle()) && todo.getDescription() != null) {
            String bookInfo = processBookChecker(todo.getDescription());
            if (bookInfo != null) {
                // Replace description with book information
                todo.setDescription(bookInfo);
            }
            // If bookInfo is null, keep original description as fallback
        }
        
        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
    }
    
    /**
     * Processes Book Checker logic - attempts to parse ID and fetch book information
     * 
     * @param description the description field containing potential book ID
     * @return formatted book info "title / author" or null if processing fails
     */
    private String processBookChecker(String description) {
        try {
            // Try to parse description as integer (book ID)
            int bookId = Integer.parseInt(description.trim());
            
            // Call external Book API via BookService
            String bookInfo = bookService.getBookInfo(bookId);
            
            if (bookInfo != null) {
                System.out.println("✅ Book Checker success: " + bookInfo);
                return bookInfo;
            } else {
                System.out.println("⚠️ Book Checker failed: No book data returned");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Book Checker failed: Description is not a valid integer");
        } catch (Exception e) {
            System.out.println("⚠️ Book Checker failed: " + e.getMessage());
        }
        
        return null; // Fallback - keep original description
    }

    /**
     * Update existing todo with validation and exception handling
     * 
     * @param id the ID of the todo to update
     * @param todoDetails the updated todo data (must be valid)
     * @return ResponseEntity with updated todo
     * @throws TodoNotFoundException if todo with given ID doesn't exist
     * @apiNote PUT /api/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todoDetails) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        
        // Update fields
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.getCompleted());
        
        Todo updatedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(updatedTodo);
    }

    /**
     * Delete todo with proper exception handling
     * 
     * @param id the ID of the todo to delete
     * @return ResponseEntity with success message
     * @throws TodoNotFoundException if todo with given ID doesn't exist
     * @apiNote DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        
        todoRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Get completed todos
     * GET /api/todos/completed
     */
    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> getCompletedTodos() {
        List<Todo> completedTodos = todoRepository.findByCompleted(true);
        return ResponseEntity.ok(completedTodos);
    }

    /**
     * Get pending todos
     * GET /api/todos/pending
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Todo>> getPendingTodos() {
        List<Todo> pendingTodos = todoRepository.findByCompleted(false);
        return ResponseEntity.ok(pendingTodos);
    }

    /**
     * Search todos by title with validation
     * 
     * @param title the title to search for (required, non-blank)
     * @return ResponseEntity with matching todos
     * @apiNote GET /api/todos/search?title=...
     */
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(
            @RequestParam @NotBlank(message = "Search title cannot be blank") String title) {
        List<Todo> todos = todoRepository.findByTitleContainingIgnoreCase(title.trim());
        return ResponseEntity.ok(todos);
    }
}
