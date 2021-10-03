package com.itechart.book_library.dao.api;

import com.itechart.book_library.entity.Author;
import com.itechart.book_library.entity.Genre;

public interface GenreDao extends Dao<Genre> {
    public Genre getByName(String name);
}
