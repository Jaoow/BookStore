package com.jaoow.bookstore.web.dto;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.model.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "ISBN is required")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be a 13-digit number")
    private String isbn;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotEmpty(message = "Publish date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Publish date must be in the format YYYY-MM-DD")
    private String publishDate;

    @NotEmpty(message = "At least one author ID is required")
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
