package com.pavel.todoapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.Duration;

/**
 * Service for calling external Book API
 * Uses WebClient for HTTP communication with timeout handling
 * 
 * @author Pavel
 * @since 1.0
 */
@Service
public class BookService {

    private final WebClient webClient;
    private static final String BOOK_API_URL = "https://simple-books-api.glitch.me";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructor initializes WebClient with base URL configuration
     */
    public BookService() {
        this.webClient = WebClient.builder()
                .baseUrl(BOOK_API_URL)
                .build();
    }

    /**
     * Retrieves book information by ID from external Book API
     * 
     * @param bookId the unique identifier of the book
     * @return formatted string "title / author" or null if error occurs
     * @throws WebClientRequestException if HTTP request fails
     */
    public String getBookInfo(int bookId) {
        try {
            // Execute GET /books/{id} with 5 second timeout
            BookResponse response = webClient
                    .get()
                    .uri("/books/{id}", bookId)
                    .retrieve()
                    .bodyToMono(BookResponse.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .block();

            if (response != null && response.name != null && response.author != null) {
                return response.name + " / " + response.author;
            }
            
        } catch (WebClientRequestException | RuntimeException e) {
            System.err.println("Error calling Book API: " + e.getMessage());
        }
        
        return null; // Fallback on error
    }

    /**
     * Data Transfer Object for Book API response
     * Maps JSON fields from external API to Java object
     */
    public static class BookResponse {
        /** Unique book identifier */
        public int id;
        
        /** Book availability status */
        public boolean available;
        
        /** Book title */
        public String name;
        
        /** Book author */
        public String author;
        
        /** Book category (fiction/non-fiction) */
        public String type;
        
        /** Book price in currency */
        public double price;
        
        /** Current stock quantity */
        public int currentStock;
    }
}
