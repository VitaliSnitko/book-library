package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.AuthorEntity;

public class AuthorDto {
    private String name;

    public AuthorDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
