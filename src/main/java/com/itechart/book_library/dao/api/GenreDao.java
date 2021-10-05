package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Genre;

import java.util.List;

public interface GenreDao extends Dao<Genre> {
    public Genre getByName(String name);
    public List<Genre> getByBookId(int id);
}
