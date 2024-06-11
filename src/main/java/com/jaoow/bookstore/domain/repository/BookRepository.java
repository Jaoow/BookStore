package com.jaoow.bookstore.domain.repository;

import com.jaoow.bookstore.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
