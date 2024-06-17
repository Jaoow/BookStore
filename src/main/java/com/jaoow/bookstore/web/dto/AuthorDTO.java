package com.jaoow.bookstore.web.dto;

import com.jaoow.bookstore.domain.model.Author;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Biography is required")
    private String biography;

    @NotEmpty(message = "Birth date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birth date must be in the format YYYY-MM-DD")
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
