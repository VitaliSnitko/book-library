package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.Author;

public class AuthorDto {
    private String name;

    public AuthorDto(String name) {
        this.name = name;
    }

    public Author convertAuthorDtoToAuthorEntity() {
        Author author = new Author();
        author.setName(name);
        return author;
    }
}
