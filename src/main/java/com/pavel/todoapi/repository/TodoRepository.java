package com.pavel.todoapi.repository;

import com.pavel.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for Todo entity operations
 * Extends JpaRepository to provide standard CRUD operations plus custom queries
 * Uses method name conventions and JPQL queries for data access
 * 
 * @author Pavel
 * @since 1.0
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    /**
     * Finds all todos filtered by completion status
     * Uses Spring Data method name convention for automatic query generation
     * 
     * @param completed true to find completed todos, false for pending todos
     * @return List of todos matching the completion status
     */
    List<Todo> findByCompleted(Boolean completed);
    
    /**
     * Finds todos containing specified text in title (case insensitive search)
     * Uses Spring Data method name convention with IgnoreCase modifier
     * 
     * @param title text to search for in todo titles
     * @return List of todos whose titles contain the search text
     */
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Counts the total number of completed todos using JPQL query
     * 
     * @return number of todos where completed status is true
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = true")
    Long countCompletedTodos();
    
    /**
     * Counts the total number of pending todos using JPQL query
     * 
     * @return number of todos where completed status is false
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = false")
    Long countPendingTodos();
}
