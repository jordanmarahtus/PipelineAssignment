package com.library.bookservice.controller;
import com.library.bookservice.model.Book;
import com.library.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    private Book sampleBook;

    @BeforeEach
    public void setup() {
        sampleBook = new Book(1L, "East of eden", "steinbeck", "abcd1234");
    }

    @Test
    public void testAddBook() throws Exception {
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"title\": \"New Book\", \"author\": \"New Author\", \"isbn\": 9999}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"));

        verify(bookRepository, times(1)).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();

        // Now actually check the saved book
        assertEquals("New Book", savedBook.getTitle());
        assertEquals("New Author", savedBook.getAuthor());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        // Act & Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("East of eden"))
                .andExpect(jsonPath("$[0].author").value("steinbeck"));
    }

    @Test
    public void testGetBookById() throws Exception {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleBook));

        // Act & Assert
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("East of eden"))
                .andExpect(jsonPath("$.author").value("steinbeck"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        // Arrange
        doNothing().when(bookRepository).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        verify(bookRepository, times(1)).deleteById(1L);
    }
}