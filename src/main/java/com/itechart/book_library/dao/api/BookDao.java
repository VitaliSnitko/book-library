package com.itechart.book_library.dao.api;

import com.itechart.book_library.dao.criteria.BookSpecification;
import com.itechart.book_library.model.entity.BookEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookDao extends Dao<BookEntity> {

    List<BookEntity> getLimitOffsetBySpecification(BookSpecification specification, int limit, int offset);

    int getCountBySpecification(BookSpecification specification);

    void takeBook(int id, Connection connection) throws SQLException;

    void loseBook(int id, Connection connection) throws SQLException;

    void returnBook(int id, Connection connection) throws SQLException;
}
