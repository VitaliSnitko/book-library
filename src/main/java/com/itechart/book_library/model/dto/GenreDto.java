package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.Author;
import com.itechart.book_library.model.entity.Genre;

public class GenreDto {
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }

    public Genre convertGenreDtoToGenreEntity() {
        Genre genre = new Genre();
        genre.setName(name);
        return genre;
    }
}
