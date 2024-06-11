package com.jaoow.bookstore.service;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.repository.AuthorRepository;
import com.jaoow.bookstore.web.dto.AuthorDTO;
import com.jaoow.bookstore.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test successful retrieval of an author by ID")
    public void testFindById_Success() {
        Author author = new Author(1L, "Lena Green", "Bestselling author known for historical novels", "1985-06-23", Collections.emptySet());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO authorDTO = authorService.findById(1L);

        assertNotNull(authorDTO);
        assertEquals("Lena Green", authorDTO.getName());
    }

    @Test
    @DisplayName("Test retrieval of an author by ID fails when author is not found")
    public void testFindById_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.findById(1L));
    }

    @Test
    @DisplayName("Test successful creation of a new author")
    public void testCreate_Success() {
        Author author = new Author(1L, "Mark Jones", "Renowned biographer", "1972-11-19", Collections.emptySet());
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Mark Jones");
        authorDTO.setBiography("Renowned biographer");
        authorDTO.setBirthDate("1972-11-19");

        AuthorDTO createdAuthor = authorService.create(authorDTO);

        assertNotNull(createdAuthor);
        assertEquals("Mark Jones", createdAuthor.getName());
    }

    @Test
    @DisplayName("Test successful update of an existing author")
    public void testUpdate_Success() {
        Long authorId = 1L;
        Author existingAuthor = new Author(authorId, "Samantha Ray", "Famous poet and essayist", "1990-03-15", Collections.emptySet());
        Author updatedAuthor = new Author(authorId, "Samantha Gray", "Award-winning poet and essayist", "1990-03-15", Collections.emptySet());

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Samantha Gray");
        authorDTO.setBiography("Award-winning poet and essayist");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        AuthorDTO result = authorService.update(authorId, authorDTO);

        assertNotNull(result);
        assertEquals("Samantha Gray", result.getName());
        verify(authorRepository).save(existingAuthor);
    }

    @Test
    @DisplayName("Test update of an author fails when author is not found")
    public void testUpdate_NotFound() {
        Long authorId = 1L;
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("James Wright");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.update(authorId, authorDTO));
    }

    @Test
    @DisplayName("Test successful deletion of an author")
    public void testDelete_Success() {
        Long authorId = 1L;

        when(authorRepository.existsById(authorId)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(authorId);

        authorService.delete(authorId);

        verify(authorRepository).deleteById(authorId);
    }

    @Test
    @DisplayName("Test deletion of an author fails when author is not found")
    public void testDelete_NotFound() {
        Long authorId = 1L;

        when(authorRepository.existsById(authorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authorService.delete(authorId));
    }
}
