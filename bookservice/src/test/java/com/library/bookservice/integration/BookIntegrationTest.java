package com.library.bookservice.integration;

import com.library.bookservice.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Ensures web environment is loaded with random port
public class BookIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book();
        testBook.setId(8L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("123456");
    }

    @Test
    public void testAddBook() {
        ResponseEntity<Book> response = restTemplate.postForEntity("/books", testBook, Book.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testGetAllBooks() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity("/books", Book[].class);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testGetBookById() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/books/8", Book.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteBook() {
        restTemplate.delete("/books/5");
        ResponseEntity<Book> response = restTemplate.getForEntity("/books/8", Book.class);
        assertEquals(200, response.getStatusCodeValue()); 
    }
}