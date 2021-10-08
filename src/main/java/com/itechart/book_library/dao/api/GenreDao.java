package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Genre;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenreDao extends Dao<Genre> {
    Optional<Genre> getByName(String name) throws SQLException;
    List<Genre> getByBookId(int id) throws SQLException;
}