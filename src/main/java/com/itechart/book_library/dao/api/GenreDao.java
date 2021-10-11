package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenreDao extends Dao<GenreEntity> {
    Optional<GenreEntity> getById(int id) throws SQLException;
    Optional<GenreEntity> getByName(String name) throws SQLException;
    List<GenreEntity> getByBookId(int id) throws SQLException;
    List<GenreEntity> getByBookList(List<BookEntity> bookEntityList) throws SQLException;
}
