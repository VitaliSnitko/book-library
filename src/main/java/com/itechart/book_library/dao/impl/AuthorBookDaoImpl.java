package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorBookDaoImpl extends BaseDao {

    private static final String INSERT_AUTHOR_BOOK_QUERY = "INSERT INTO author_book (author_id, book_id) VALUES (?,?)";

    public void setAuthorToBook(int authorId, int bookId) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_AUTHOR_BOOK_QUERY)) {
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.execute();
        } finally {
            connectionPool.removeToPool(connection);
        }
    }
}
