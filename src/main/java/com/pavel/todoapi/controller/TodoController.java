package com.pavel.todoapi.controller;

import com.pavel.todoapi.entity.Todo;
import com.pavel.todoapi.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Todo API
 * Provides CRUD operations via HTTP endpoints
 * 
 * @author Pavel
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // Allow all origins for testing
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    /**
     * Get API info and statistics
     * GET /api/todos/info
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
     * Get todo by ID
     * GET /api/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new todo
     * POST /api/todos
     */
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        try {
            Todo savedTodo = todoRepository.save(todo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update existing todo
     * PUT /api/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setTitle(todoDetails.getTitle());
            todo.setDescription(todoDetails.getDescription());
            todo.setCompleted(todoDetails.getCompleted());
            
            Todo updatedTodo = todoRepository.save(todo);
            return ResponseEntity.ok(updatedTodo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete todo
     * DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Todo deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
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
     * Search todos by title
     * GET /api/todos/search?title=...
     */
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(@RequestParam String title) {
        List<Todo> todos = todoRepository.findByTitleContainingIgnoreCase(title);
        return ResponseEntity.ok(todos);
    }
}
