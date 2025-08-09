package com.pavel.todoapi.config;

import com.pavel.todoapi.entity.Todo;
import com.pavel.todoapi.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer - creates sample todos on application startup
 * 
 * @author Pavel
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data (in case of restart)
        todoRepository.deleteAll();
        
        // Create sample todos
        Todo todo1 = new Todo("Vytvořit REST API", "Implementovat Spring Boot aplikaci s CRUD operacemi");
        
        Todo todo2 = new Todo("Testovat v Postmanu", "Otestovat všechny endpointy pomocí Postman");
        
        Todo todo3 = new Todo("Deploy na web", "Nasadit aplikaci na Render nebo Railway");
        
        Todo todo4 = new Todo("Dokončit dokumentaci", "Napsat README a API dokumentaci");
        todo4.setCompleted(true);
        
        Todo todo5 = new Todo("Přidat validace", "Implementovat validaci vstupních dat");
        
        // Save sample data
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
        todoRepository.save(todo4);
        todoRepository.save(todo5);
        
        System.out.println("✅ Sample data initialized!");
        System.out.println("📊 Created " + todoRepository.count() + " sample todos");
    }
}
