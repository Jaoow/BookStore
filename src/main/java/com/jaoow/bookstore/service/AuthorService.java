package com.jaoow.bookstore.service;

import com.jaoow.bookstore.domain.model.Author;
import com.jaoow.bookstore.domain.repository.AuthorRepository;
import com.jaoow.bookstore.web.dto.AuthorDTO;
import com.jaoow.bookstore.web.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AuthorDTO findById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        Author author = authorDTO.toEntity();
        return AuthorDTO.fromEntity(authorRepository.save(author));
    }

    public AuthorDTO update(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));

        existingAuthor.setName(authorDTO.getName());
        existingAuthor.setBiography(authorDTO.getBiography());
        existingAuthor.setBirthDate(authorDTO.getBirthDate());

        return AuthorDTO.fromEntity(authorRepository.save(existingAuthor));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
    }
}
