package com.jaoow.bookstore.domain.repository;

import com.jaoow.bookstore.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
