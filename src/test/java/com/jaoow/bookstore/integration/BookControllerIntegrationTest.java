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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        bookRepository.save(new Book(null, "The Adventure Begins", "9787482614772", 25.0, "2023-01-01", new HashSet<>()));
        bookRepository.save(new Book(null, "Secrets of the Unknown", "9781146208680", 30.0, "2023-02-01", new HashSet<>()));

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
        bookDTO.setIsbn("9783395812434");
        bookDTO.setPrice(25.0);
        bookDTO.setPublishDate("2023-01-01");
        bookDTO.setAuthorIds(Set.of(1L));

        ResponseEntity<BookDTO> response = restTemplate.postForEntity(getBaseUrl() + "/api/books", bookDTO, BookDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The Secret Garden", response.getBody().getTitle());
    }

    @Test
    @DisplayName("Test creating a book with invalid data")
    public void testCreateBook_InvalidData() {
        BookDTO bookDTO = new BookDTO();
        HttpEntity<BookDTO> request = new HttpEntity<>(bookDTO);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                getBaseUrl() + "/api/books", HttpMethod.POST, request,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertTrue(errors.containsKey("title"), "Error: Title field validation failed");
        assertTrue(errors.containsKey("isbn"), "Error: ISBN field validation failed");
        assertTrue(errors.containsKey("price"), "Error: Price field validation failed");
        assertTrue(errors.containsKey("publishDate"), "Error: Publish date field validation failed");
    }

    @Test
    @DisplayName("Test creating a book with an invalid ISBN")
    public void testCreateBook_InvalidISBN() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Invalid ISBN Book");
        bookDTO.setIsbn("InvalidISBN");  // Invalid ISBN
        bookDTO.setPrice(20.0);
        bookDTO.setPublishDate("2023-01-01");
        bookDTO.setAuthorIds(new HashSet<>());

        HttpEntity<BookDTO> request = new HttpEntity<>(bookDTO);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                getBaseUrl() + "/api/books", HttpMethod.POST, request,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertTrue(errors.containsKey("isbn"), "Error: ISBN validation failed");
        assertEquals("ISBN must be a 13-digit number", errors.get("isbn"));
    }

    @Test
    @DisplayName("Test updating a book with invalid price")
    public void testUpdateBook_InvalidPrice() {
        long bookId = 1L;

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Valid Title");
        bookDTO.setIsbn("1234567890123");
        bookDTO.setPrice(-10.0); // Invalid Price (negative)
        bookDTO.setPublishDate("2023-01-01");
        bookDTO.setAuthorIds(new HashSet<>());

        HttpEntity<BookDTO> request = new HttpEntity<>(bookDTO);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                getBaseUrl() + "/api/books/" + bookId, HttpMethod.PUT, request,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertTrue(errors.containsKey("price"), "Error: Price validation failed");
        assertEquals("Price must be a positive number", errors.get("price"));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
