package com.jaoow.bookstore.web.dto;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.model.Book;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private Double price;
    private String publishDate;
    private Set<Long> authorIds;

    public static BookDTO fromEntity(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setPublishDate(book.getPublishDate());
        bookDTO.setAuthorIds(book.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
        return bookDTO;
    }

    public Book toEntity(Set<Author> authors) {
        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setIsbn(this.isbn);
        book.setPrice(this.price);
        book.setPublishDate(this.publishDate);
        book.setAuthors(authors);
        return book;
    }
}
