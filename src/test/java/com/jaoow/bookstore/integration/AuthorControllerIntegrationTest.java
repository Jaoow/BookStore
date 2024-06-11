package com.jaoow.bookstore.integration;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.repository.AuthorRepository;
import com.jaoow.bookstore.web.dto.AuthorDTO;
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
public class AuthorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Test retrieving all authors returns a list of authors")
    public void testGetAllAuthors() {
        authorRepository.save(new Author(null, "Alice Walker", "Famous for novels and essays", "1944-02-09", null));
        authorRepository.save(new Author(null, "George Orwell", "Renowned for dystopian novels", "1903-06-25", null));

        ResponseEntity<AuthorDTO[]> response = restTemplate.getForEntity(getBaseUrl() + "/api/authors", AuthorDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    @DisplayName("Test retrieving a specific author by ID")
    public void testGetAuthorById() {
        Author author = authorRepository.save(new Author(null, "J.K. Rowling", "Known for the Harry Potter series", "1965-07-31", null));
        ResponseEntity<AuthorDTO> response = restTemplate.getForEntity(getBaseUrl() + "/api/authors/" + author.getId(), AuthorDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("J.K. Rowling", response.getBody().getName());
    }

    @Test
    @DisplayName("Test creating a new author")
    public void testCreateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Toni Morrison");
        authorDTO.setBiography("Celebrated for her rich narratives and characters");
        authorDTO.setBirthDate("1931-02-18");

        ResponseEntity<AuthorDTO> response = restTemplate.postForEntity(getBaseUrl() + "/api/authors", authorDTO, AuthorDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Toni Morrison", response.getBody().getName());
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
