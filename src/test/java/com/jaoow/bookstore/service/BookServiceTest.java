package com.jaoow.bookstore.service;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.model.Book;
import com.jaoow.bookstore.domain.repository.AuthorRepository;
import com.jaoow.bookstore.domain.repository.BookRepository;
import com.jaoow.bookstore.web.dto.BookDTO;
import com.jaoow.bookstore.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test successful creation of a book")
    public void testCreate_Success() {
        Author author = new Author(1L, "Jane Doe", "Award-winning novelist", "1980-02-28", Collections.emptySet());
        Book book = new Book(1L, "The Lost Chronicles", "987654321", 19.99, "2024-05-14", Set.of(author));

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("The Lost Chronicles");
        bookDTO.setIsbn("987654321");
        bookDTO.setPrice(19.99);
        bookDTO.setPublishDate("2024-05-14");
        bookDTO.setAuthorIds(Set.of(1L));

        when(authorRepository.findAllById(bookDTO.getAuthorIds())).thenReturn(Stream.of(author).collect(Collectors.toList()));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO createdBook = bookService.create(bookDTO);

        assertNotNull(createdBook);
        assertEquals("The Lost Chronicles", createdBook.getTitle());
        assertTrue(createdBook.getAuthorIds().contains(1L));
    }

    @Test
    @DisplayName("Test successful update of a book")
    public void testUpdate_Success() {
        Long bookId = 1L;
        Author author = new Author(1L, "John Smith", "Bestselling author", "1975-11-12", Collections.emptySet());
        Book existingBook = new Book(bookId, "The Old Man and the Sea", "1122334455", 14.99, "2023-03-11", Collections.emptySet());
        Book updatedBook = new Book(bookId, "The New Voyage", "1122334455", 18.99, "2024-08-22", Set.of(author));

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("The New Voyage");
        bookDTO.setIsbn("1122334455");
        bookDTO.setPrice(18.99);
        bookDTO.setPublishDate("2024-08-22");
        bookDTO.setAuthorIds(Set.of(1L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findAllById(bookDTO.getAuthorIds())).thenReturn(Stream.of(author).collect(Collectors.toList()));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        BookDTO result = bookService.update(bookId, bookDTO);

        assertNotNull(result);
        assertEquals("The New Voyage", result.getTitle());
        assertTrue(result.getAuthorIds().contains(1L));
        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("Test update of a book fails when book is not found")
    public void testUpdate_NotFound() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("The Journey Ahead");
        bookDTO.setAuthorIds(Set.of(1L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(bookId, bookDTO));
    }

    @Test
    @DisplayName("Test successful deletion of a book")
    public void testDelete_Success() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.delete(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Test deletion of a book fails when book is not found")
    public void testDelete_NotFound() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(bookId));
    }
}
