package com.pavel.todoapi.repository;

import com.pavel.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Todo entity
 * Provides CRUD operations and custom queries
 * 
 * @author Pavel
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    /**
     * Find all todos by completion status
     * @param completed true for completed todos, false for pending
     * @return List of todos with specified completion status
     */
    List<Todo> findByCompleted(Boolean completed);
    
    /**
     * Find todos containing title (case insensitive)
     * @param title title to search for
     * @return List of todos containing the title
     */
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Count completed todos
     * @return number of completed todos
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = true")
    Long countCompletedTodos();
    
    /**
     * Count pending todos
     * @return number of pending todos
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = false")
    Long countPendingTodos();
}
