package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.GenreEntity;

public class GenreDto {
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
