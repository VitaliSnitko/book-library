package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Genre;

public interface GenreDao extends Dao<Genre> {
    public Genre getByName(String name);
}
