package com.pavel.todoapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
    
    // 📋 URL Configuration - Easy to manage multiple endpoints
    // ✅ Active URLs (will be tried in order)
    private static final String[] ACTIVE_BOOK_API_URLS = {
        "https://simple-books-api.click",        // Primary - current working URL
        // "https://simple-books-api.glitch.me", // Add future URLs here
    };
    
    // ❌ Inactive URLs (for reference/rollback)
    private static final String[] INACTIVE_BOOK_API_URLS = {
        "https://simple-books-api.glitch.me",    // Old URL - redirects but slower
        // Add other deprecated URLs here for reference
    };
    
    private static final int TIMEOUT_SECONDS = 2;
    private static final int MAX_RETRIES = 3;

    /**
     * Constructor initializes WebClient with primary URL
     * Other URLs will be tried as fallbacks if primary fails
     */
    public BookService() {
        // Use first active URL as primary
        String primaryUrl = ACTIVE_BOOK_API_URLS.length > 0 ? ACTIVE_BOOK_API_URLS[0] : "https://simple-books-api.click";
        this.webClient = WebClient.builder()
                .baseUrl(primaryUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
        
        System.out.println("🌐 BookService initialized with primary URL: " + primaryUrl);
        if (ACTIVE_BOOK_API_URLS.length > 1) {
            System.out.println("🔄 Fallback URLs available: " + (ACTIVE_BOOK_API_URLS.length - 1));
        }
    }

    /**
     * Retrieves book information by ID with automatic URL fallback
     * Tries all active URLs until one works
     * 
     * @param bookId the unique identifier of the book
     * @return formatted string "title / author" or null if all URLs fail
     */
    public String getBookInfo(int bookId) {
        // Try all active URLs in order
        for (int urlIndex = 0; urlIndex < ACTIVE_BOOK_API_URLS.length; urlIndex++) {
            String currentUrl = ACTIVE_BOOK_API_URLS[urlIndex];
            String result = tryUrlWithRetry(currentUrl, bookId, urlIndex + 1);
            
            if (result != null) {
                return result; // Success with this URL
            }
            
            System.out.println("⚠️ URL " + (urlIndex + 1) + "/" + ACTIVE_BOOK_API_URLS.length + " failed: " + currentUrl);
        }
        
        System.err.println("❌ All active URLs failed for book ID: " + bookId);
        return null; // All URLs failed
    }
    
    /**
     * Tries a specific URL with retry mechanism
     * 
     * @param baseUrl the base URL to try
     * @param bookId the book ID to fetch
     * @param urlNumber which URL number this is (for logging)
     * @return book info or null if this URL fails
     */
    private String tryUrlWithRetry(String baseUrl, int bookId, int urlNumber) {
        System.out.println("🌐 Trying URL " + urlNumber + ": " + baseUrl);
        
        // Create WebClient for this specific URL
        WebClient urlWebClient = WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
        
        // Try this URL with retries
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            String result = tryUrlSingleAttempt(urlWebClient, baseUrl, bookId, urlNumber, attempt);
            if (result != null) {
                return result; // Success!
            }
            
            // Wait before retry (except on last attempt)
            if (attempt < MAX_RETRIES) {
                try {
                    long delayMs = 500L * attempt;
                    System.out.println("🔄 URL " + urlNumber + " retry in " + delayMs + "ms...");
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        
        return null; // This URL failed after all retries
    }
    
    /**
     * Single attempt to call API with specific URL
     * 
     * @param urlWebClient WebClient configured for this URL
     * @param baseUrl the base URL being tried
     * @param bookId the book ID
     * @param urlNumber URL number for logging
     * @param attempt attempt number for logging
     * @return book info or null if failed
     */
    private String tryUrlSingleAttempt(WebClient urlWebClient, String baseUrl, int bookId, int urlNumber, int attempt) {
        try {
            System.out.println("📞 URL " + urlNumber + " attempt " + attempt + "/" + MAX_RETRIES + " for book ID: " + bookId);
            
            BookResponse response = urlWebClient
                    .get()
                    .uri("/books/{id}", bookId)
                    .retrieve()
                    .bodyToMono(BookResponse.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .block();

            if (response != null && response.name != null && response.author != null) {
                System.out.println("✅ SUCCESS with URL " + urlNumber + " on attempt " + attempt + ": " + response.name);
                return response.name + " / " + response.author;
            }
            
        } catch (WebClientResponseException e) {
            // Handle redirects
            if (e.getStatusCode().is3xxRedirection()) {
                String location = e.getHeaders().getFirst("Location");
                if (location != null) {
                    System.out.println("🔄 URL " + urlNumber + " redirects to: " + location);
                    return handleRedirectSimple(location, bookId);
                }
            }
            System.err.println("📋 URL " + urlNumber + " HTTP error: " + e.getStatusCode());
        } catch (Exception e) {
            if (e.getCause() instanceof java.util.concurrent.TimeoutException || 
                e.getMessage().contains("timeout")) {
                System.err.println("⏰ URL " + urlNumber + " timeout (after " + TIMEOUT_SECONDS + "s)");
            } else {
                System.err.println("💥 URL " + urlNumber + " error: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    /**
     * Simplified redirect handler
     */
    private String handleRedirectSimple(String location, int bookId) {
        try {
            String newBaseUrl = location.substring(0, location.lastIndexOf("/books"));
            WebClient redirectClient = WebClient.builder().baseUrl(newBaseUrl).build();
            
            BookResponse response = redirectClient
                    .get()
                    .uri("/books/{id}", bookId)
                    .retrieve()
                    .bodyToMono(BookResponse.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .block();

            if (response != null && response.name != null && response.author != null) {
                return response.name + " / " + response.author;
            }
        } catch (Exception e) {
            System.err.println("❌ Redirect failed: " + e.getMessage());
        }
        return null;
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
