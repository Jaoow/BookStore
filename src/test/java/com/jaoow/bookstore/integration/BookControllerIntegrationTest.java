package com.jaoow.bookstore.integration;

import com.jaoow.bookstore.domain.model.Book;
import com.jaoow.bookstore.domain.repository.BookRepository;
import com.jaoow.bookstore.web.dto.BookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test retrieving all books returns a list of books")
    public void testGetAllBooks() {
        bookRepository.save(new Book(null, "The Adventure Begins", "123456789", 25.0, "2023-01-01", null));
        bookRepository.save(new Book(null, "Secrets of the Unknown", "987654321", 30.0, "2023-02-01", null));

        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity(getBaseUrl() + "/api/books", BookDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    @DisplayName("Test retrieving a specific book by ID")
    public void testGetBookById() {
        Book book = bookRepository.save(new Book(null, "The Mystery of the Lost Key", "123456789", 25.0, "2023-01-01", null));

        ResponseEntity<BookDTO> response = restTemplate.getForEntity(getBaseUrl() + "/api/books/" + book.getId(), BookDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The Mystery of the Lost Key", response.getBody().getTitle());
    }

    @Test
    @DisplayName("Test creating a new book")
    public void testCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("The Secret Garden");
        bookDTO.setIsbn("123456789");
        bookDTO.setPrice(25.0);
        bookDTO.setPublishDate("2023-01-01");

        ResponseEntity<BookDTO> response = restTemplate.postForEntity(getBaseUrl() + "/api/books", bookDTO, BookDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The Secret Garden", response.getBody().getTitle());
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
