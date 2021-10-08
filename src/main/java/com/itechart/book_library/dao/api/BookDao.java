package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.BookEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDao extends Dao<BookEntity>{
    Optional<BookEntity> getByTitle(String title) throws SQLException;
    List<BookEntity> getByAuthor(int id) throws SQLException;
    List<BookEntity> getByGenre(int id) throws SQLException;
    List<BookEntity> getByDescription(String description) throws SQLException;
}
