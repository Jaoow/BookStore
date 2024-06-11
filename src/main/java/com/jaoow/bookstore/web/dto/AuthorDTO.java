package com.jaoow.bookstore.web.dto;

import com.jaoow.bookstore.domain.model.Author;
import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;
    private String name;
    private String biography;
    private String birthDate;

    public static AuthorDTO fromEntity(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setBiography(author.getBiography());
        authorDTO.setBirthDate(author.getBirthDate());
        return authorDTO;
    }

    public Author toEntity() {
        Author author = new Author();
        author.setId(this.id);
        author.setName(this.name);
        author.setBiography(this.biography);
        author.setBirthDate(this.birthDate);
        return author;
    }
}
