package com.jaoow.bookstore.service;


import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.model.Book;
import com.jaoow.bookstore.domain.repository.AuthorRepository;
import com.jaoow.bookstore.domain.repository.BookRepository;
import com.jaoow.bookstore.web.dto.BookDTO;
import com.jaoow.bookstore.web.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO findById(Long id) {
        return bookRepository.findById(id)
                .map(BookDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    public BookDTO create(BookDTO bookDTO) {
        Set<Author> authors = new HashSet<>();
        if (bookDTO.getAuthorIds() != null) {
            authors.addAll(authorRepository.findAllById(bookDTO.getAuthorIds()));
        }

        Book book = bookDTO.toEntity(authors);
        return BookDTO.fromEntity(bookRepository.save(book));
    }

    public BookDTO update(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorIds()));
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setPublishDate(bookDTO.getPublishDate());
        book.setAuthors(authors);

        return BookDTO.fromEntity(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }
}
