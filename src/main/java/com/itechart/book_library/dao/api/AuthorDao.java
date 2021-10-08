package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuthorDao extends Dao<Author> {
    Optional<Author> getByName(String name) throws SQLException;
    List<Author> getByBookId(int id) throws SQLException;
}
